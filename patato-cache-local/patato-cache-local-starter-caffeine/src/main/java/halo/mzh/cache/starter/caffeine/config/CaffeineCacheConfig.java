package halo.mzh.cache.starter.caffeine.config;

import halo.mzh.cache.spring.support.SpringInvokeSupport;
import halo.mzh.cache.starter.caffeine.config.properties.CaffeineCacheProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author shoufeng
 */

@Configuration
@ComponentScan(basePackages = {"halo.mzh.cache.starter.caffeine"})
@EnableConfigurationProperties(value = {
        CaffeineCacheProperties.class
})
@ConditionalOnProperty(prefix = CaffeineCacheProperties.PREFIX, value = "enable")
@Slf4j
@Data
public class CaffeineCacheConfig {

    @Bean
    public SpringInvokeSupport springInvokeSupport() {
        return new SpringInvokeSupport();
    }

}
