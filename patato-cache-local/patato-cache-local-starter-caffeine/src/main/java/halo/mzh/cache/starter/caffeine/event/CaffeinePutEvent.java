package halo.mzh.cache.starter.caffeine.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author shoufeng
 */

public class CaffeinePutEvent<V> extends ApplicationEvent {


    public CaffeinePutEvent(CaffeinePutEventInfo caffeinePutHolder) {
        super(caffeinePutHolder);
    }
}
