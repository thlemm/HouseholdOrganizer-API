package de.thlemm.householdorganizer.repository;


import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.Transaction;
import de.thlemm.householdorganizer.model.TransactionStatus;
import de.thlemm.householdorganizer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Transaction findById(Long id);
    boolean existsById(Long id);
    List<Transaction> findAllByTransactionStatus(TransactionStatus transactionStatus);
}
