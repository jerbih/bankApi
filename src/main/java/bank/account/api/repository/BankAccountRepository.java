package bank.account.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import bank.account.api.model.BankAccount;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {	
	
	//Optional<BankAccount> findById(Long number);
         
}
