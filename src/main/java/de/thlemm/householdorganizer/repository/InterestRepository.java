package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Interest;
import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {
    Interest findById(Long id);
    Interest findByUserAndItem(User user, Item item);
    boolean existsByUserAndItem(User user, Item item);
}
