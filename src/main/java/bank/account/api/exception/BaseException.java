
package bank.account.api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * General exception to build a specific message from error code
 * @author hjerbi 
 */
@Getter
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 4929598769902018240L;

    /**
     * Erros filed to set error keys messages
     */
    private final Map<String, String> errorKeys;

    /**
     * httpStatus field
     */
    private final HttpStatus httpStatus;

    
    public BaseException(HttpStatus status, Map<String, String> errors) {
        super();
        this.errorKeys = errors;
        this.httpStatus = status;
    }
    
    public BaseException() {
    	super();
    	this.errorKeys = new HashMap<>();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    public BaseException(Map<String, String> errors) {
        super();
        this.errorKeys = errors;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    public BaseException(HttpStatus status, String message, Map<String, String> errors) {
        super(message);
        this.httpStatus = status;
        this.errorKeys = errors;
   
    }

  
}
