package halo.mzh.cache.starter.caffeine.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.*;
import halo.mzh.cache.serializer.support.KryoSerializerSupport;
import halo.mzh.cache.starter.caffeine.config.properties.CaffeineCacheProperties;
import halo.mzh.cache.starter.caffeine.generator.CaffeineKeyGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
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
    private CaffeineKeyGenerator caffeineKeyGenerator;

    private ObjectMapper objectMapper = new ObjectMapper();

    public Cache<String, byte[]> generateDefaultCache() {

        return Caffeine.newBuilder()
                .expireAfterAccess(caffeineCacheProperties.getExpireAfterAccess(), TimeUnit.SECONDS)
                .expireAfterWrite(caffeineCacheProperties.getExpireAfterWrite(), TimeUnit.SECONDS)
                .refreshAfterWrite(caffeineCacheProperties.getRefreshAfterWrite(), TimeUnit.SECONDS)
                .removalListener(new RemovalListener<String, byte[]>() {
                    @SneakyThrows
                    @Override
                    public void onRemoval(@Nullable String key, byte @Nullable [] bytes, @NonNull RemovalCause removalCause) {
                        if (removalCause.name().equalsIgnoreCase(RemovalCause.EXPIRED.name()) || removalCause.name().equalsIgnoreCase(RemovalCause.EXPLICIT.name())) {
                            caffeineKeyGenerator.getCacheKeyInvokeParamMap().remove(key);
                            log.warn("键: {}, 移除执行参数invokeParam", key);
                        }
                        Object value = KryoSerializerSupport.instance().decode(bytes);
                        log.info("缓存: {},值: {}, 被移除: {}", key, value, removalCause);
                    }
                }).build(new CacheLoader<String, byte[]>() {
                    @Override
                    public byte @Nullable [] load(@NonNull String key) throws Exception {
                        CaffeineKeyGenerator.InvokeParam invokeParam = caffeineKeyGenerator.getCacheKeyInvokeParamMap().get(key);
                        if (ObjectUtils.isEmpty(invokeParam)) {
                            log.warn("键: {}, 获取执行参数invokeParam失败", key);
                        }
                        Object obj = invokeParam.getObject();
                        Method objMethod = invokeParam.getObjMethod();
                        Object[] args = invokeParam.getArgs();
                        Object result = objMethod.invoke(obj, args);
                        return KryoSerializerSupport.instance().encode(result);
                    }
                });

    }

}
