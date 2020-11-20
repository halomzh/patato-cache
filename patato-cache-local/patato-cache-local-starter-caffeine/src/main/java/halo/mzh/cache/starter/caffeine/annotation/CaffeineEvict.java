package halo.mzh.cache.starter.caffeine.annotation;

import java.lang.annotation.*;

/**
 * @author shoufeng
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CaffeineEvict {

    /**
     * 缓存名称空间
     */
    String nameSpace() default "";

    /**
     * 缓存名称
     */
    String[] names() default {};

}
