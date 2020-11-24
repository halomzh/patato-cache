package halo.mzh.cache.spring.support.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shoufeng
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PatatoCacheSpringSupportException extends RuntimeException {

    private static final long serialVersionUID = -6199666474403507816L;
    
    private String message;
    private int code = 500;

    public PatatoCacheSpringSupportException(String message) {
        super(message);
        this.message = message;
    }

    public PatatoCacheSpringSupportException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public PatatoCacheSpringSupportException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public PatatoCacheSpringSupportException(String message, int code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

}
