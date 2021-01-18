package bank.account.api.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

public class InvalidOperationException extends BaseException {


	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5313170798344581455L;

	public InvalidOperationException(String errorKey, String errorField) {
		super(HttpStatus.BAD_REQUEST, Map.of(errorKey,errorField));        
	}

	public InvalidOperationException(String message, Map<String, String> errors) {
		super(HttpStatus.BAD_REQUEST, message, errors);
	}

}