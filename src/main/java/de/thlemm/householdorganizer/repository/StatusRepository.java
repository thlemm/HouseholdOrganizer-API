package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Status;
import de.thlemm.householdorganizer.model.StatusName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, String> {
    Status findByName(StatusName name);
}