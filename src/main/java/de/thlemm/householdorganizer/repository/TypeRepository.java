package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.ItemType;
import de.thlemm.householdorganizer.model.TypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<ItemType, Integer> {
    ItemType findByName(TypeName name);
    ItemType findById(Long id);
    boolean existsById(Long id);
}
