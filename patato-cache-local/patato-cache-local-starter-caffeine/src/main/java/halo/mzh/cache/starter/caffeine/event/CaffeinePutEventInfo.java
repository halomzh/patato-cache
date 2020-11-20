package halo.mzh.cache.starter.caffeine.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shoufeng
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaffeinePutEventInfo<V> {

    /**
     * 缓存名称空间
     */
    private String nameSpace;

    /**
     * 缓存名称
     */
    private String name;

    /**
     * 缓存key
     */
    private String key;

    /**
     * 缓存对象
     */
    private V value;
    
}
