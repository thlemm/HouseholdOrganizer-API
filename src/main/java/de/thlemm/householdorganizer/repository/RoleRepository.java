package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Role;
import de.thlemm.householdorganizer.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleName name);
}
