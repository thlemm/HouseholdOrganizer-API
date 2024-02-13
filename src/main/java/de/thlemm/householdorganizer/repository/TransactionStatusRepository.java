package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.TransactionStatus;
import de.thlemm.householdorganizer.model.TransactionStatusName;
import de.thlemm.householdorganizer.model.UserStatus;
import de.thlemm.householdorganizer.model.UserStatusName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionStatusRepository extends JpaRepository<TransactionStatus, String> {
    TransactionStatus findByName(TransactionStatusName name);
    TransactionStatus findById(Long id);
    boolean existsById(Long id);
}