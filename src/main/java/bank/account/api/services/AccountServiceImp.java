package bank.account.api.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bank.account.api.exception.AccountNotFoundException;
import bank.account.api.exception.BaseException;
import bank.account.api.exception.InvalidOperationException;
import bank.account.api.model.BankAccount;
import bank.account.api.model.Transaction;
import bank.account.api.model.Transaction.TransactionType;
import bank.account.api.repository.BankAccountRepository;

@Service
public class AccountServiceImp implements AccountService {

	private static final String INVALID_ACCOUNT_NUMBER = "customer.account.number.invalid"; 
	private static final String INVALID_AMOUNT = "customer.account.amount.invalid"; 
	private static final String NOT_ENOUGH_CASH = "customer.account.amount.exceeds"; 
	
	@Autowired
	private BankAccountRepository accountRepository;
	
	
	@Override
	public List<BankAccount> getAccounts() {
		List<BankAccount> accounts = new ArrayList<BankAccount>();
		accountRepository.findAll().forEach(e->accounts.add(e));;
		return accounts;
	}

	@Override
	public BankAccount getAccount(Long accountNumber) throws BaseException {	
		BankAccount account = accountRepository.findByNumber(Long.valueOf(accountNumber));
		if (account == null) {
			throw new AccountNotFoundException(INVALID_ACCOUNT_NUMBER, "accountNumber");
		}
		
		return account;
	}

	@Override
	public boolean isAccountExist(Long accountNumber) {        
		return accountRepository.findByNumber(accountNumber) != null;
	}

	@Override
	public List<Transaction> getHistory(Long accountNumber) throws BaseException {
		BankAccount account = accountRepository.findByNumber(accountNumber);
		if (account == null) {
			throw new AccountNotFoundException(INVALID_ACCOUNT_NUMBER, "accountNumber");
		}
		return account.getHistory();
	}

	@Override
	public double getBalance(Long accountNumber) throws BaseException {
		BankAccount account = accountRepository.findByNumber(accountNumber);
		if (account == null) {
			throw new AccountNotFoundException(INVALID_ACCOUNT_NUMBER, "accountNumber");
		}		
		
		return account.getBalance();
	}
	
	@Override
	public Long deposit(Long accountNumber, double amount, String comment) throws BaseException {
		return makeOperation(TransactionType.DEPOSIT, accountNumber, amount, comment);
	}

	@Override
	public Long withDraw(Long accountNumber, double amount, String comment) throws BaseException {			
		return makeOperation(TransactionType.WITHDRAWAL, accountNumber, amount, comment);
	}
	
	/**
	 * make deposit or withdraw operation
	 * @param type
	 * @param accountNumber
	 * @param amount
	 * @param comment
	 * @return return transaction id
	 */
	private Long makeOperation (TransactionType type, Long accountNumber, double amount, String comment) {		
		BankAccount account = accountRepository.findByNumber(accountNumber);
		if (account == null) {
			throw new AccountNotFoundException(INVALID_ACCOUNT_NUMBER, "accountNumber");
		}
		
		if (amount <= 0) {
			throw new InvalidOperationException(INVALID_AMOUNT, "amount");
		}
		
		if (TransactionType.WITHDRAWAL.equals(type) && account.getBalance() < amount) {
			throw new InvalidOperationException(NOT_ENOUGH_CASH, "amount");
		}
		
		if (TransactionType.WITHDRAWAL.equals(type)) {
			amount = -amount;
		}
		
		double newBalance = account.getBalance() + amount;
		account.setBalance(newBalance);
		
		Transaction transaction = Transaction.builder().date(LocalDateTime.now()).comment(comment).bankaccount(account).transactionType(type).amount(amount).build();
		account.getHistory().add(transaction);
		
		
		accountRepository.save(account);
		
		return transaction.getId();
	}
	


}
