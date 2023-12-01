package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.UserStatus;
import de.thlemm.householdorganizer.model.UserStatusName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, String> {
    UserStatus findByName(UserStatusName name);
}