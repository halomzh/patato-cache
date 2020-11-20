package halo.mzh.cache.caffeine;

import com.github.benmanes.caffeine.cache.CacheLoader;

/**
 * @author shoufeng
 */

@FunctionalInterface
public interface CaffeineCacheLoader<K, V> extends CacheLoader<K, V> {
}
