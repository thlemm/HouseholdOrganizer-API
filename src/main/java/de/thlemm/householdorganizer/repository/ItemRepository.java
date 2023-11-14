package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Item findById(Long id);
    boolean existsById(Long id);
    @Query(value="SELECT item.*, t.type_id, o.room_id, c.room_id FROM household_organizer.items item" +
            " INNER JOIN household_organizer.interests interest ON item.id = interest.item_id" +
            " INNER JOIN household_organizer.items_types t ON item.id = t.item_id" +
            " INNER JOIN household_organizer.items_original_rooms o ON item.id = o.item_id" +
            " INNER JOIN household_organizer.items_current_rooms c ON item.id = c.item_id" +
            " WHERE interest.user_id = :userId" +
            " ORDER BY id ASC", nativeQuery = true)
    List<Item> findAllOfInterestByUserId(Long userId);
}
