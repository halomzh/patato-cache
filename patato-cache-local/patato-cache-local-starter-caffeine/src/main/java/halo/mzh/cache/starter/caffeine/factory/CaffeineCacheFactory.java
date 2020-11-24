package halo.mzh.cache.starter.caffeine.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import halo.mzh.cache.caffeine.CaffeineCacheLoader;
import halo.mzh.cache.caffeine.CaffeineRemovalListener;
import halo.mzh.cache.serializer.support.KryoSerializerSupport;
import halo.mzh.cache.spring.support.SpringPCacheSupport;
import halo.mzh.cache.starter.caffeine.config.properties.CaffeineCacheProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author shoufeng
 */

@Slf4j
@Component
public class CaffeineCacheFactory {

    @Autowired
    private CaffeineCacheProperties caffeineCacheProperties;

    @Autowired
    private SpringPCacheSupport springPCacheSupport;

    private ObjectMapper objectMapper = new ObjectMapper();

    public Cache<String, byte[]> generateDefaultCache() {

        @NonNull Caffeine<Object, Object> builder = Caffeine.newBuilder();

        if (caffeineCacheProperties.getExpireAfterAccess() > 0) {
            builder.expireAfterAccess(caffeineCacheProperties.getExpireAfterAccess(), TimeUnit.SECONDS);
        }

        if (caffeineCacheProperties.getExpireAfterWrite() > 0) {
            builder.expireAfterWrite(caffeineCacheProperties.getExpireAfterWrite(), TimeUnit.SECONDS);
        }

        return builder
                .maximumSize(caffeineCacheProperties.getMaximumSize())
                .refreshAfterWrite(caffeineCacheProperties.getRefreshAfterWrite(), TimeUnit.SECONDS)
                .removalListener(new CaffeineRemovalListener<String, byte[]>() {
                    @SneakyThrows
                    @Override
                    @Async
                    public void onRemoval(@Nullable String key, byte @Nullable [] bytes, @NonNull RemovalCause removalCause) {
                        if (removalCause.name().equalsIgnoreCase(RemovalCause.EXPIRED.name()) || removalCause.name().equalsIgnoreCase(RemovalCause.EXPLICIT.name())) {
                            Map<String, SpringPCacheSupport.InvokeParam> cacheKeyInvokeParamMap = springPCacheSupport.getCacheTypeCacheKeyInvokeParamMapMap().get(CaffeineCacheProperties.PREFIX);
                            cacheKeyInvokeParamMap.remove(key);
                            log.warn("键: {}, 移除执行参数invokeParam", key);
                        }
                        Object value = KryoSerializerSupport.instance().decode(bytes);
                        log.info("缓存: {},值: {}, 被移除: {}", key, value, removalCause);
                    }
                }).build(new CaffeineCacheLoader<String, byte[]>() {
                    @Override
                    public byte @Nullable [] load(@NonNull String key) throws Exception {
                        Object result = springPCacheSupport.invokeByKeyAndCachePrefix(CaffeineCacheProperties.PREFIX, key);
//                        SpringPCacheSupport.InvokeParam invokeParam = springPCacheSupport.getCacheTypeCacheKeyInvokeParamMapMap().get(CaffeineCacheProperties.PREFIX).get(key);
//                        if (ObjectUtils.isEmpty(invokeParam)) {
//                            log.warn("键: {}, 获取执行参数invokeParam失败", key);
//                        }
//                        Object obj = invokeParam.getObject();
//                        Method objMethod = invokeParam.getObjMethod();
//                        Object[] args = invokeParam.getArgs();
//                        Object result = objMethod.invoke(obj, args);
                        return KryoSerializerSupport.instance().encode(result);
                    }

                    @Override
                    public byte @Nullable [] reload(@NonNull String key, byte @NonNull [] oldValue) throws Exception {
                        byte[] newValue = load(key);
                        log.info("刷新key: {}, 旧值: {}, 新值: {}", key, KryoSerializerSupport.instance().decode(oldValue), KryoSerializerSupport.instance().decode(newValue));
                        return newValue;
                    }
                });

    }

}
