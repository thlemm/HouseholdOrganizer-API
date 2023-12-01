package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.UserType;
import de.thlemm.householdorganizer.model.UserTypeName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType, String> {
    UserType findByName(UserTypeName name);
}