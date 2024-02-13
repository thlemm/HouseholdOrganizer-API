package de.thlemm.householdorganizer.repository;


import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.Transaction;
import de.thlemm.householdorganizer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Transaction findById(Long id);
    boolean existsById(Long id);
}
