package halo.mzh.cache.caffeine;

import java.util.function.Function;

/**
 * @author shoufeng
 */

@FunctionalInterface
public interface CaffeineCacheLoader<K, V> extends Function<K, V> {
}
