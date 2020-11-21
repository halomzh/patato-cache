package halo.mzh.cache.starter.caffeine.aspect;

import halo.mzh.cache.spring.support.generator.SpringCacheKeyGenerateSupport;
import halo.mzh.cache.starter.caffeine.annotation.CaffeinePut;
import halo.mzh.cache.starter.caffeine.config.properties.CaffeineCacheProperties;
import halo.mzh.cache.starter.caffeine.event.CaffeinePutEvent;
import halo.mzh.cache.starter.caffeine.event.CaffeinePutEventInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author shoufeng
 */

@Data
@Aspect
@Component
@Slf4j
public class CaffeinePutAspect implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private SpringCacheKeyGenerateSupport springCacheKeyGenerateSupport;

    @Pointcut("@annotation(halo.mzh.cache.starter.caffeine.annotation.CaffeinePut)")
    public void caffeinePutPointcut() {
    }

    @Around("caffeinePutPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Object value = point.proceed(args);

        Class<?> classTarget = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, parameterTypes);

        CaffeinePut caffeinePut = objMethod.getAnnotation(CaffeinePut.class);

        String nameSpace = caffeinePut.nameSpace();
        String name = caffeinePut.name();

        String key = springCacheKeyGenerateSupport.generateKey(CaffeineCacheProperties.PREFIX, nameSpace, name, point);

        CaffeinePutEvent caffeinePutEvent = new CaffeinePutEvent(new CaffeinePutEventInfo(nameSpace, name, key, value));
        applicationContext.publishEvent(caffeinePutEvent);

        return value;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
