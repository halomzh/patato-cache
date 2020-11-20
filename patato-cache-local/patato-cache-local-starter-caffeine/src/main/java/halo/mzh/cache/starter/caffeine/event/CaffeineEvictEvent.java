package halo.mzh.cache.starter.caffeine.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author shoufeng
 */

public class CaffeineEvictEvent extends ApplicationEvent {


    public CaffeineEvictEvent(CaffeineEvictEventInfo caffeineEvictHolder) {
        super(caffeineEvictHolder);
    }

}
