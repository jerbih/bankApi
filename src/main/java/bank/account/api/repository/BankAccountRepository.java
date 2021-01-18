package bank.account.api.repository;

import org.springframework.data.repository.CrudRepository;

import bank.account.api.model.BankAccount;

public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {	

		public BankAccount findByNumber(Long number);
		
         
}
