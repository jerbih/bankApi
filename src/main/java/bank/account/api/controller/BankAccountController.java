package bank.account.api.controller;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bank.account.api.exception.AccountNotFoundException;
import bank.account.api.exception.BaseException;
import bank.account.api.exception.ClientErrorException;
import bank.account.api.model.BankAccount;
import bank.account.api.model.Transaction;
import bank.account.api.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BankAccountController {

	@Autowired
	private AccountService accountService;

	@GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Operation(summary = "Get all bank accounts")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "The bank account is not empty", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = BankAccount.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", 
			content = @Content) })	
	public ResponseEntity<List<BankAccount>> getAccounts () {
		List<BankAccount> accounts = new ArrayList<>();
		try {	
			accounts = accountService.getAccounts();
		} catch (Exception e) {
			log.error("Technical error {} ", e.getMessage());
			throw new ClientErrorException(e.getMessage());
		}

		return new ResponseEntity <> (accounts, HttpStatus.OK);
	}

	@GetMapping(value = "/accounts/{identifier}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get bank account by number")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "The bank account is found", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = BankAccount.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", 
			content = @Content), 
			@ApiResponse(responseCode = "404", description = "Bank account is not found", 
			content = @Content) })	
	public ResponseEntity<BankAccount> getAccount (final @PathVariable @NotEmpty Long identifier) throws Exception {
		BankAccount bankAccount = null;
		try {
			bankAccount = accountService.getAccount(identifier);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			log.error("Technical error {} ", e.getMessage());
			throw new ClientErrorException(e.getMessage());
		}	
		return new ResponseEntity <> (bankAccount, HttpStatus.OK);
	}

	@GetMapping(value = "/accounts/{identifier}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get the balance of the given bank account number")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Return the balance", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = BankAccount.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", 
			content = @Content),	
			@ApiResponse(responseCode = "404", description = "Bank account is not found", 
			content = @Content) })
	public ResponseEntity<Double> getBalance (final @PathVariable @NotEmpty Long identifier) throws Exception {
		BankAccount bankAccount = null;
		try {
			bankAccount = accountService.getAccount(identifier);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			log.error("Technical error {} ", e.getMessage());
			throw new ClientErrorException(e.getMessage());
		}		
		return new ResponseEntity <> (bankAccount.getBalance(), HttpStatus.OK);
	}

	@GetMapping(value = "/accounts/{identifier}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get the balance of the given bank account number")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Return the balance of the given bank account", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = BankAccount.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", 
			content = @Content),	
			@ApiResponse(responseCode = "404", description = "Bank account is not found", 
			content = @Content) })
	public ResponseEntity<List<Transaction>> getHistory (final @PathVariable @NotEmpty Long identifier) throws Exception {
		List<Transaction> history = new ArrayList<>();
		try {
			if (accountService.isAccountExist(identifier)) {
				throw new AccountNotFoundException("Account is not Found","number");
			}
			history = accountService.getHistory(identifier);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			log.error("Technical error {} ", e.getMessage());
			throw new ClientErrorException(e.getMessage());
		}		
		return new ResponseEntity <> (history, HttpStatus.OK);
	}

	@PostMapping(value = "/accounts/{identifier}/deposit", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get the balance of the given bank account number")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Return the balance of the given bank account", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = BankAccount.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", 
			content = @Content),	
			@ApiResponse(responseCode = "404", description = "Bank account is not found", 
			content = @Content) })
	public ResponseEntity<Long> deposit (final @PathVariable @NotEmpty Long identifier,
			final @RequestParam(required = true) @Positive double ammount, 
			final @RequestParam(required = false) @Positive String comment) throws Exception {
		Long transactionId = null;
		try {
			if (!accountService.isAccountExist(identifier)) {
				throw new AccountNotFoundException("Account is not Found","number");
			}
			transactionId = accountService.deposit(identifier, ammount, comment);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			log.error("Technical error {} ", e.getMessage());
			throw new ClientErrorException(e.getMessage());
		}		
		return new ResponseEntity <> (transactionId, HttpStatus.OK);
	}

	@PostMapping(value = "/accounts/{identifier}/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get the balance of the given bank account number")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Return the balance of the given bank account", 
					content = { @Content(mediaType = "application/json", 
					schema = @Schema(implementation = BankAccount.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", 
			content = @Content),	
			@ApiResponse(responseCode = "404", description = "Bank account is not found", 
			content = @Content) })
	public ResponseEntity<Long> withDraw (final @PathVariable @NotEmpty Long identifier,
			final @RequestParam(required = true) @Positive double ammount, 
			final @RequestParam(required = false) @Positive String comment) throws Exception {
		Long transactionId = null;
		try {
			if (!accountService.isAccountExist(identifier)) {
				throw new AccountNotFoundException("Account is not Found","number");
			}
			transactionId = accountService.withDraw(identifier, ammount, comment);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			log.error("Technical error {} ", e.getMessage());
			throw new ClientErrorException(e.getMessage());
		}		
		return new ResponseEntity <> (transactionId, HttpStatus.OK);
	}


}
