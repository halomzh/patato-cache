package halo.mzh.cache.starter.caffeine.listener;

import com.github.benmanes.caffeine.cache.Cache;
import halo.mzh.cache.serializer.support.KryoSerializerSupport;
import halo.mzh.cache.starter.caffeine.event.CaffeinePutEvent;
import halo.mzh.cache.starter.caffeine.event.CaffeinePutEventInfo;
import halo.mzh.cache.starter.caffeine.factory.CaffeineCacheFactory;
import halo.mzh.cache.starter.caffeine.holder.CaffeineCacheHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author shoufeng
 */

@Slf4j
@Component
public class CaffeinePutEventListener {

    @Autowired
    private CaffeineCacheHolder caffeineCacheHolder;

    @Autowired
    private CaffeineCacheFactory caffeineCacheFactory;

    @Async
    @Order
    @EventListener(CaffeinePutEvent.class)
    public void doCaffeinePutEvent(CaffeinePutEvent caffeinePutEvent) throws IOException {
        CaffeinePutEventInfo caffeinePutEventInfo = (CaffeinePutEventInfo) caffeinePutEvent.getSource();
        Map<String, Cache<String, byte[]>> nameSpaceCacheMap = caffeineCacheHolder.getNameSpaceCacheMap();
        Cache<String, byte[]> cache = nameSpaceCacheMap.get(caffeinePutEventInfo.getNameSpace());
        if (ObjectUtils.isEmpty(cache)) {
            cache = caffeineCacheFactory.generateDefaultCache();
        }
        cache.put(caffeinePutEventInfo.getKey(), KryoSerializerSupport.instance().encode(caffeinePutEventInfo.getValue()));
        caffeineCacheHolder.getNameSpaceCacheMap().put(caffeinePutEventInfo.getNameSpace(), cache);
    }

}
