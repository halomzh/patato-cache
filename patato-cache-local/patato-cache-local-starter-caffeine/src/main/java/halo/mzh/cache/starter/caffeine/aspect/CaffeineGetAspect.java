package halo.mzh.cache.starter.caffeine.aspect;

import com.github.benmanes.caffeine.cache.Cache;
import halo.mzh.cache.serializer.support.KryoSerializerSupport;
import halo.mzh.cache.spring.support.generator.SpringCacheKeyGenerateSupport;
import halo.mzh.cache.starter.caffeine.annotation.CaffeineGet;
import halo.mzh.cache.starter.caffeine.config.properties.CaffeineCacheProperties;
import halo.mzh.cache.starter.caffeine.factory.CaffeineCacheFactory;
import halo.mzh.cache.starter.caffeine.holder.CaffeineCacheHolder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author shoufeng
 */

@Data
@Aspect
@Component
@Slf4j
public class CaffeineGetAspect {

    @Autowired
    private CaffeineCacheHolder caffeineCacheHolder;

    @Autowired
    private SpringCacheKeyGenerateSupport springCacheKeyGenerateSupport;

    @Autowired
    private CaffeineCacheFactory caffeineCacheFactory;

    @Pointcut("@annotation(halo.mzh.cache.starter.caffeine.annotation.CaffeineGet)")
    public void caffeineGetPointcut() {
    }

    @Around("caffeineGetPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Class<?> classTarget = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, parameterTypes);

        CaffeineGet caffeineGet = objMethod.getAnnotation(CaffeineGet.class);

        String nameSpace = caffeineGet.nameSpace();
        String name = caffeineGet.name();

        String key = springCacheKeyGenerateSupport.generateKey(CaffeineCacheProperties.PREFIX, nameSpace, name, point);

        Cache<String, byte[]> cache = caffeineCacheHolder.getNameSpaceCacheMap().get(nameSpace);
        if (ObjectUtils.isEmpty(cache)) {
            cache = caffeineCacheFactory.generateDefaultCache();
        }

        caffeineCacheHolder.getNameSpaceCacheMap().put(nameSpace, cache);

        return KryoSerializerSupport.instance().decode(cache.get(key, new Function<String, byte[]>() {
            @SneakyThrows
            @Override
            public byte[] apply(String key) {
                return KryoSerializerSupport.instance().encode(point.proceed(args));
            }
        }));

    }
}
