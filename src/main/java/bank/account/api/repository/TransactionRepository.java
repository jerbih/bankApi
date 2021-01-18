package bank.account.api.repository;

import org.springframework.data.repository.CrudRepository;

import bank.account.api.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}
