package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.ItemType;
import de.thlemm.householdorganizer.model.ItemTypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTypeRepository extends JpaRepository<ItemType, Integer> {
    ItemType findByName(ItemTypeName name);
    ItemType findById(Long id);
    boolean existsById(Long id);
}
