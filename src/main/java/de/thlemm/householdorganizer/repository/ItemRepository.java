package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Item findById(Long id);
    boolean existsById(Long id);
}
