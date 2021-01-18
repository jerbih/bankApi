package bank.account.api.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import bank.account.api.BankAccountApplication;
import bank.account.api.exception.AccountNotFoundException;
import bank.account.api.exception.InvalidOperationException;
import bank.account.api.model.BankAccount;
import bank.account.api.model.Transaction;
import bank.account.api.repository.BankAccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=BankAccountApplication.class)
@ActiveProfiles("test")
public class AccountServiceTest {

	private static int BANK_ACCOUNT_TABLE_SIZE = 10;

	@InjectMocks
	private AccountServiceImp accountService;

	@Mock
	private BankAccountRepository accountRepository;

	private List<BankAccount> bankAccounts = new ArrayList<>();


	@BeforeEach
	public void init() throws IllegalAccessException {
		initMocks(this);
		bankAccounts = buildAccounts();
	}

	@Test
	public void get_bank_accounts_return_10 () {
		Mockito.when(accountRepository.findAll()).thenReturn(bankAccounts);		
		assertEquals(BANK_ACCOUNT_TABLE_SIZE, accountService.getAccounts().size());      
	}

	@Test
	public void isAccountExist_return_false_when_account_doesnt_exist () {
		Mockito.when(accountRepository.findByNumber(20l)).thenReturn(null);		
		assertFalse(accountService.isAccountExist(20l));      
	}

	@Test
	public void isAccountExist_return_true_when_account_exist () {
		//GIVEN
		Mockito.when(accountRepository.findByNumber(1l)).thenReturn(bankAccounts.get(0));		
		//THEN
		assertTrue(accountService.isAccountExist(1l));      
	}

	@Test
	public void getBalance_throws_exception_when_account_doesnt_exist () {
		//GIVEN
		Mockito.when(accountRepository.findByNumber(20l)).thenReturn(null);

		//THEN
		AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
			accountService.getBalance(20l);
		});

		String expectedMessage = "customer.account.number.invalid";
		assertTrue(HttpStatus.NOT_FOUND == exception.getHttpStatus());
		assertTrue(exception.getErrorKeys().containsKey(expectedMessage));

	}

	@Test
	public void getBalance_return_customer_account_balance_when_account_exist () {
		//GIVEN
		Mockito.when(accountRepository.findByNumber(2l)).thenReturn(bankAccounts.get(1));

		//THEN
		double balance = accountService.getBalance(2l);

		assertEquals(100, balance);	

	}

	@Test
	public void getHistory_throws_exception_when_account_doesnt_exist () {
		//GIVEN
		Mockito.when(accountRepository.findByNumber(20l)).thenReturn(null);

		//WHEN

		AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
			accountService.getHistory(20l);
		});

		//THEN
		String expectedMessage = "customer.account.number.invalid";
		assertTrue(HttpStatus.NOT_FOUND == exception.getHttpStatus());
		assertTrue(exception.getErrorKeys().containsKey(expectedMessage));
	}

	@Test
	public void getHistory_return_customer_account_history_when_account_exist () {
		//GIVEN
		Mockito.when(accountRepository.findByNumber(1l)).thenReturn(bankAccounts.get(0));

		//WHEN
		List<Transaction> history = accountService.getHistory(1l);

		//THEN
		assertEquals(2, history.size());
		assertEquals(1, history.get(0).getBankaccount().getNumber());
	}

	@Test
	public void deposit_throws_exception_when_account_doesnt_exist () {
		//GIVEN
		Mockito.when(accountRepository.findByNumber(20l)).thenReturn(null);

		//WHEN
		AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
			accountService.deposit(20l, 200, "Deposit money");
		});

		//THEN
		String expectedMessage = "customer.account.number.invalid";
		assertTrue(HttpStatus.NOT_FOUND == exception.getHttpStatus());
		assertTrue(exception.getErrorKeys().containsKey(expectedMessage));
	}
	
	@Test
	public void deposit_modify_bank_account_balance_when_account_exist_and_amount_valid () {
		//GIVEN
		BankAccount account = bankAccounts.get(3);
		Mockito.when(accountRepository.findByNumber(4l)).thenReturn(account);
		
		Mockito.when(accountRepository.save(account)).thenReturn(account);

		//Check  balance before the withdrawl
		assertEquals(200, account.getBalance());
		
		//WHEN
		accountService.deposit(4l, 500, "cash deposit");

		//THEN
		assertEquals(700, account.getBalance());

	}
	
	
	@Test
	public void withDraw_throws_exception_when_amount_not_valid () {
		//GIVEN
		Mockito.when(accountRepository.findByNumber(1l)).thenReturn(bankAccounts.get(0));

		//WHEN
		InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
			accountService.withDraw(1l, -60, "Deposit money");
		});

		//THEN
		String expectedMessage = "customer.account.amount.invalid";
		assertTrue(HttpStatus.BAD_REQUEST == exception.getHttpStatus());
		assertTrue(exception.getErrorKeys().containsKey(expectedMessage));
	}
	
	@Test
	public void withDraw_throws_exception_when_amount_greater_than_balance () {
		//GIVEN
		Mockito.when(accountRepository.findByNumber(2l)).thenReturn(bankAccounts.get(1));

		//WHEN
		InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
			accountService.withDraw(2l, 200, "Money deposit");
		});

		//THEN
		String expectedMessage = "customer.account.amount.exceeds";
		assertTrue(HttpStatus.BAD_REQUEST == exception.getHttpStatus());
		assertTrue(exception.getErrorKeys().containsKey(expectedMessage));
	}
	
	@Test
	public void withDraw_throws_exception_when_account_doesnt_exist () {
		
		//GIVEN
		Mockito.when(accountRepository.findByNumber(1l)).thenReturn(null);

		//WHEN
		AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
			accountService.withDraw(20l, 200, "Deposit money");
		});

		//THEN
		String expectedMessage = "customer.account.number.invalid";
		assertTrue(HttpStatus.NOT_FOUND == exception.getHttpStatus());
		assertTrue(exception.getErrorKeys().containsKey(expectedMessage));
	}
	
	@Test
	public void withDraw_modify_bank_account_balance_when_account_exist_and_amount_valid () {
		//GIVEN
		BankAccount account = bankAccounts.get(2);
		Mockito.when(accountRepository.findByNumber(3l)).thenReturn(account);
		
		Mockito.when(accountRepository.save(account)).thenReturn(account);

		//Check the balance before the withdrawl
		assertEquals(150, account.getBalance());
		
		//WHEN
		accountService.withDraw(3l, 50, "cash withdrawal");

		//THEN
		assertEquals(100, account.getBalance());

	}
	
	private List<BankAccount> buildAccounts () {
		BankAccount account = null;

		for (int i = 1; i <= BANK_ACCOUNT_TABLE_SIZE; i++) {
			account = BankAccount.builder().number(Long.valueOf(i)).balance(50*i).history(new ArrayList<>()).build();
			bankAccounts.add(account);			
		}

		// bank account balance set to 70d
		Transaction transaction = Transaction.builder().id(1l).amount(20d)
				.comment("deposit money").date(LocalDateTime.now())
				.bankaccount(bankAccounts.get(0)).build();	

		bankAccounts.get(0).getHistory().add(transaction);		
		// bank account balance set to 270d
		Transaction transaction1 = Transaction.builder().id(2l).amount(200d).comment("deposit money")
				.date(LocalDateTime.now()).bankaccount(bankAccounts.get(0)).build();

		bankAccounts.get(0).setBalance(270);
		bankAccounts.get(0).getHistory().add(transaction1);

		return bankAccounts;
	}

}
