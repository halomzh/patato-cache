package halo.mzh.cache.spring.support;

import halo.mzh.cache.spring.support.exception.PatatoCacheSpringSupportException;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author shoufeng
 */

public class SpringInvokeSupport implements ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringInvokeSupport.applicationContext = applicationContext;
    }

    public static ConfigurableApplicationContext getConfigurableApplicationContext() {
        return (ConfigurableApplicationContext) applicationContext;
    }

    public static Object invoke(Class clazz, String method, Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Object bean = applicationContext.getBean(clazz);

        if (ObjectUtils.isEmpty(bean)) {
            throw new PatatoCacheSpringSupportException("获取bean失败: " + clazz);
        }
        Method declaredMethod = null;
        if (ObjectUtils.isEmpty(args) || args.length == 0) {
            declaredMethod = bean.getClass().getDeclaredMethod(method);
        } else {
            Class[] types = new Class[args.length];
            declaredMethod = bean.getClass().getDeclaredMethod(method, types);
        }

        return declaredMethod.invoke(bean, args);

    }

}
