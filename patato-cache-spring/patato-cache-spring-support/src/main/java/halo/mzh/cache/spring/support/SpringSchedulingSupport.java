package halo.mzh.cache.spring.support;

import halo.mzh.cache.spring.support.config.SpringSchedulingConfig;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shoufeng
 */

public class SpringSchedulingSupport {

    private static Map<Runnable, Long> fixedDelayTaskMap = new ConcurrentHashMap<>();

    private static Map<String, Runnable> keyFixedDelayTaskMap = new ConcurrentHashMap<>();

    private static ScheduledTaskRegistrar scheduledTaskRegistrar;

    public static ScheduledTaskRegistrar getScheduledTaskRegistrar() {

        if (ObjectUtils.isEmpty(scheduledTaskRegistrar)) {
            SpringSchedulingConfig springSchedulingConfig = SpringPCacheSupport.getApplicationContext().getBean(SpringSchedulingConfig.class);
            scheduledTaskRegistrar = springSchedulingConfig.getTaskRegistrar();
        }

        return scheduledTaskRegistrar;

    }

    public static void addFixedDelayTask(String key, Runnable task, long interval) {

        keyFixedDelayTaskMap.put(key, task);
        fixedDelayTaskMap.put(task, interval);

        refresh();

    }

    public static void removeFixedDelayTask(String cacheKey) {

        Runnable task = keyFixedDelayTaskMap.get(cacheKey);
        fixedDelayTaskMap.remove(task);

        refresh();

    }


    public static void refresh() {

        ScheduledTaskRegistrar scheduledTaskRegistrar = getScheduledTaskRegistrar();
        scheduledTaskRegistrar.setFixedDelayTasks(fixedDelayTaskMap);
        ConfigurableApplicationContext applicationContext = SpringPCacheSupport.getConfigurableApplicationContext();

        applicationContext.refresh();

    }

}
