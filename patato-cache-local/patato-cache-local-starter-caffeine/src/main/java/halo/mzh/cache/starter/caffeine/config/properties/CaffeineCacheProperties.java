package halo.mzh.cache.starter.caffeine.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shoufeng
 */

@Data
@ConfigurationProperties(prefix = CaffeineCacheProperties.PREFIX)
@NoArgsConstructor
@AllArgsConstructor
public class CaffeineCacheProperties {

    public static final String PREFIX = "pcaffeine";

    /**
     * 是否开启
     */
    private boolean enable = false;

    /**
     * 缓存的最大条数, 默认1024条
     */
    private long maximumSize = 1024;

    /**
     * 最后一次写入或访问后经过固定时间过期，默认不过期
     */
    private long expireAfterAccess = -1;

    /**
     * 最后一次写入后经过固定时间过期，默认不过期
     * expireAfterWrite和expireAfterAccess同时存在时，以expireAfterWrite为准。
     */
    private long expireAfterWrite = -1;

    /**
     * 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存，默认1分钟
     */
    private long refreshAfterWrite = 60;

}
