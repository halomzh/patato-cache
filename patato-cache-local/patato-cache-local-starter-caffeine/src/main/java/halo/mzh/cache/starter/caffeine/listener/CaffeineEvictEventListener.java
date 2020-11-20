package halo.mzh.cache.starter.caffeine.listener;

import com.github.benmanes.caffeine.cache.Cache;
import halo.mzh.cache.starter.caffeine.event.CaffeineEvictEvent;
import halo.mzh.cache.starter.caffeine.event.CaffeineEvictEventInfo;
import halo.mzh.cache.starter.caffeine.holder.CaffeineCacheHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author shoufeng
 */

@Slf4j
@Component
public class CaffeineEvictEventListener {

    @Autowired
    private CaffeineCacheHolder caffeineCacheHolder;

    @Async
    @Order
    @EventListener(CaffeineEvictEvent.class)
    public void doCaffeineEvictEvent(CaffeineEvictEvent caffeineEvictEvent) {
        CaffeineEvictEventInfo caffeineEvictEventInfo = (CaffeineEvictEventInfo) caffeineEvictEvent.getSource();
        Map<String, Cache<String, byte[]>> nameSpaceCacheMap = caffeineCacheHolder.getNameSpaceCacheMap();
        Cache<String, byte[]> cache = nameSpaceCacheMap.get(caffeineEvictEventInfo.getNameSpace());
        if (ObjectUtils.isNotEmpty(cache)) {
            cache.invalidate(caffeineEvictEventInfo.getKey());
        }
    }

}
