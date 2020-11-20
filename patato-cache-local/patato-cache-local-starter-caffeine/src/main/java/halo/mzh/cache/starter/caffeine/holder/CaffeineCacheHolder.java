package halo.mzh.cache.starter.caffeine.holder;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shoufeng
 */

@Data
@Component
public class CaffeineCacheHolder {

    private Map<String, Cache<String, byte[]>> nameSpaceCacheMap = new ConcurrentHashMap<>();

}
