package bank.account.api.services;

import java.util.List;

import bank.account.api.exception.BaseException;
import bank.account.api.model.BankAccount;
import bank.account.api.model.Transaction;

public interface AccountService {
	
	public List<BankAccount> getAccounts();
	public BankAccount getAccount(Long accountNumber) throws BaseException;
	public double getBalance(Long accountNumber) throws BaseException;
	public boolean isAccountExist(Long accountNumber);
	public List<Transaction> getHistory(Long accountNumber) throws BaseException;
	public Long deposit (Long accountNumber, double ammount, String comment) throws BaseException;
	public Long withDraw (Long accountNumber, double ammount, String comment) throws BaseException;
	

}
