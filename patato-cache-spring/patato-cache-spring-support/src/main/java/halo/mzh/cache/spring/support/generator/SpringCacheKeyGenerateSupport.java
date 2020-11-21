package halo.mzh.cache.spring.support.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shoufeng
 */

@Data
@Slf4j
@Component
public class SpringCacheKeyGenerateSupport {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 用于SpEL表达式解析
     */
    private SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private Map<String, Map<String, InvokeParam>> cacheTypeCacheKeyInvokeParamMapMap = new ConcurrentHashMap<>();

    public String generateKey(String cacheType, String nameSpace, String name, ProceedingJoinPoint point) throws IOException, NoSuchMethodException {

        Class<?> classTarget = point.getTarget().getClass();
        String fullPathClassName = classTarget.getTypeName();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();

        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, parameterTypes);

        Map<String, Object> cacheKeyMap = new HashMap<>();

        String[] parameterNames = nameDiscoverer.getParameterNames(objMethod);

        Object nameObject = null;
        try {
            Expression expression = parser.parseExpression(name);
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < args.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
            nameObject = expression.getValue(context);
        } catch (Exception e) {
            log.info(fullPathClassName + "." + methodName + "的SpEL表达式转换异常", e);
        }

        String cacheNameSpace = StringUtils.isEmpty(nameSpace) ? fullPathClassName + "." + methodName : nameSpace;
        Object cacheName = ObjectUtils.isEmpty(nameObject) ? args : nameObject;
        cacheKeyMap.put("nameSpace", cacheNameSpace);
        cacheKeyMap.put("name", cacheName);

        String cacheKey = objectMapper.writeValueAsString(cacheKeyMap);

        Map<String, InvokeParam> cacheKeyInvokeParamMap = cacheTypeCacheKeyInvokeParamMapMap.getOrDefault(cacheType, new ConcurrentHashMap<>());
        cacheKeyInvokeParamMap.putIfAbsent(cacheKey, new InvokeParam(point.getTarget(), objMethod, args));
        cacheTypeCacheKeyInvokeParamMapMap.put(cacheType, cacheKeyInvokeParamMap);

        return cacheKey;
    }

    @Data
    @AllArgsConstructor
    public static class InvokeParam {
        private Object object;
        private Method objMethod;
        private Object[] args;
    }

}
