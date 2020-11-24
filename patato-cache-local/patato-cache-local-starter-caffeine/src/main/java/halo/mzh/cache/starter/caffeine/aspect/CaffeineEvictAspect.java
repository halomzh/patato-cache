package halo.mzh.cache.starter.caffeine.aspect;

import halo.mzh.cache.spring.support.SpringPCacheSupport;
import halo.mzh.cache.starter.caffeine.annotation.CaffeineEvict;
import halo.mzh.cache.starter.caffeine.config.properties.CaffeineCacheProperties;
import halo.mzh.cache.starter.caffeine.event.CaffeineEvictEvent;
import halo.mzh.cache.starter.caffeine.event.CaffeineEvictEventInfo;
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
public class CaffeineEvictAspect implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private SpringPCacheSupport springPCacheSupport;

    @Pointcut("@annotation(halo.mzh.cache.starter.caffeine.annotation.CaffeineEvict)")
    public void caffeineEvictPointcut() {
    }

    @Around("caffeineEvictPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Class<?> classTarget = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, parameterTypes);

        CaffeineEvict caffeineEvict = objMethod.getAnnotation(CaffeineEvict.class);

        String nameSpace = caffeineEvict.nameSpace();
        String[] names = caffeineEvict.names();

        for (String name : names) {
            String key = springPCacheSupport.generateKey(CaffeineCacheProperties.PREFIX, nameSpace, name, point);
            CaffeineEvictEvent caffeineEvictEvent = new CaffeineEvictEvent(new CaffeineEvictEventInfo(nameSpace, name, key));
            applicationContext.publishEvent(caffeineEvictEvent);
        }

        return point.proceed(args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
