package halo.mzh.cache.caffeine;

import com.github.benmanes.caffeine.cache.RemovalListener;

/**
 * @author shoufeng
 */

public interface CaffeineRemovalListener<K, V> extends RemovalListener<K, V> {
}
