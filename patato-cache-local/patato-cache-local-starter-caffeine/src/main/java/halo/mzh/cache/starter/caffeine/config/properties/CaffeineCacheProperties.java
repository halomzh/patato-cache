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
     * 最后一次写入或访问后经过固定时间过期，默认3分钟
     */
    private long expireAfterAccess = 3 * 60;

    /**
     * 最后一次写入后经过固定时间过期，默认3分钟
     */
    private long expireAfterWrite = 3 * 60;

    /**
     * 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存，默认3分钟
     */
    private long refreshAfterWrite = 3 * 60;

}
