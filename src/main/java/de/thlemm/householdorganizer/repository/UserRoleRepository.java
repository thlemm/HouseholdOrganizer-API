package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.UserRole;
import de.thlemm.householdorganizer.model.UserRoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    UserRole findByName(UserRoleName name);
    UserRole findById(Long id);
}
