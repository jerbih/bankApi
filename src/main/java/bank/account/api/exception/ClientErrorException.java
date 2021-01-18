package bank.account.api.exception;

import java.util.Map;

public class ClientErrorException extends BaseException {

    private static final long serialVersionUID = 1645170092596084396L;
    
    /**
     * Key to display general client error
     */
    public static final String CLIENT_ERROR = "error.client";
    
	public ClientErrorException(String errorMessage) {
		super(Map.of(CLIENT_ERROR,errorMessage));        
	}
}
