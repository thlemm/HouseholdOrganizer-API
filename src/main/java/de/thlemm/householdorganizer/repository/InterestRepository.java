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
    boolean existsByUserAndItemAndInterestedTrue(User user, Item item);
    boolean existsByItemAndInterestedTrue(Item item);
    boolean existsById(Long id);
    boolean existsByItemAndUser(Item item, User user);
    boolean existsByItem(Item item);
    List<Interest> findAllByItem(Item item);

}
