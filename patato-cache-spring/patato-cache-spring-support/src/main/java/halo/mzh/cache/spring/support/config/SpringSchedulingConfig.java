package halo.mzh.cache.spring.support.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author shoufeng
 */

@Data
@Configuration
@EnableScheduling
public class SpringSchedulingConfig implements SchedulingConfigurer {

    private ScheduledTaskRegistrar taskRegistrar;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        this.taskRegistrar = taskRegistrar;
    }

}
