package halo.mzh.cache.starter.caffeine.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shoufeng
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CaffeineCacheException extends RuntimeException {

    private static final long serialVersionUID = 5947473807320125408L;
    
    private String message;
    private int code = 500;

    public CaffeineCacheException(String message) {
        super(message);
        this.message = message;
    }

    public CaffeineCacheException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public CaffeineCacheException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public CaffeineCacheException(String message, int code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

}
