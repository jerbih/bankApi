package bank.account.api.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends BaseException {


	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5313170798344581455L;

	public AccountNotFoundException(String errorKey, String errorField) {
		super(HttpStatus.NOT_FOUND,"Account not found", Map.of(errorKey,errorField));        
	}

	public AccountNotFoundException(String message, Map<String, String> errors) {
		super(HttpStatus.NOT_FOUND, message, errors);
	}

}