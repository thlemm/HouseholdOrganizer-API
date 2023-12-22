package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    String JOIN_NECESSARY_FIELDS_FOR_MAPPING =
            " INNER JOIN household_organizer.items_types it ON item.id = it.item_id" +
            " INNER JOIN household_organizer.tags ta ON item.id = ta.item_id" +
            " INNER JOIN household_organizer.items_original_rooms o ON item.id = o.item_id" +
            " INNER JOIN household_organizer.items_locations l ON item.id = l.item_id";
    Item findById(Long id);
    List<Item> findAllById(Long id);
    boolean existsById(Long id);
    @Query(value="SELECT DISTINCT ON(item.id) * FROM household_organizer.items item" +
            " INNER JOIN household_organizer.interests interest ON item.id = interest.item_id" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE interest.user_id = :userId AND interest.interested = True" +
            " ORDER BY item.id DESC", nativeQuery = true)
    List<Item> findAllOfInterestByUserId(Long userId);

    @Query(value="SELECT DISTINCT ON(item.id) * FROM household_organizer.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE it.type_id = :typeId" +
            " AND ta.tag ILIKE ANY(string_to_array(:tags,','))", nativeQuery = true)
    List<Item> findAllByTypeAndTags(Long typeId, String tags);

    @Query(value="SELECT DISTINCT ON(item.id) * FROM household_organizer.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE ta.tag ILIKE ANY(string_to_array(:tags,','))", nativeQuery = true)
    List<Item> findAllByTags(String tags);

    @Query(value="SELECT DISTINCT ON(item.id) * FROM household_organizer.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE it.type_id = :typeId", nativeQuery = true)
    List<Item> findAllByType(Long typeId);

    List<Item> findAllByLocation(Location location);
    boolean existsByLocation(Location location);
    boolean existsByMark(Long mark);
    Item findTopByOrderByMarkDesc();
    Item findByMark(Long mark);
    List<Item> findAllByMark(Long mark);

    @Query(value="SELECT DISTINCT ON(item.id) * FROM household_organizer.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE NOT EXISTS(" +
            "SELECT 1 FROM household_organizer.interests interest " +
            "WHERE interest.user_id = :userId AND interest.item_id = item.id)" +
            " ORDER BY item.id ASC LIMIT 1", nativeQuery = true)
    Item findTopNotAssessedByUserId(Long userId);

    @Query(value="SELECT * FROM household_organizer.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " ORDER BY item.id DESC LIMIT 1", nativeQuery = true)
    Item findTopById();
}
