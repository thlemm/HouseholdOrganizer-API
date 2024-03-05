package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    // ToDo: Replace this with a view
    String JOIN_NECESSARY_FIELDS_FOR_MAPPING =
            " INNER JOIN thlemmde_household.items_types item_type ON item.id = item_type.item_id" +
            " INNER JOIN thlemmde_household.tags tag ON item.id = tag.item_id" +
            " INNER JOIN thlemmde_household.items_original_rooms room ON item.id = room.item_id" +
            " INNER JOIN thlemmde_household.items_locations location ON item.id = location.item_id";
    Item findById(Long id);
    List<Item> findAllById(Long id);
    boolean existsById(Long id);
    @Query(value="SELECT * FROM thlemmde_household.items item" +
            " INNER JOIN thlemmde_household.interests interest ON item.id = interest.item_id" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE interest.user_id = :userId AND interest.interested = 1" +
            " GROUP BY item.id ORDER BY item.id DESC", nativeQuery = true)
    List<Item> findAllOfInterestByUserId(Long userId);

    List<Item> findAllByLocation(Location location);
    boolean existsByLocation(Location location);
    boolean existsByMark(Long mark);
    Item findTopByOrderByMarkDesc();
    Item findByMark(Long mark);
    List<Item> findAllByMark(Long mark);

    // ToDo: Replace with function based on TRANSACTION_STATUS_NOT_ASSESSED flag
    @Query(value="SELECT DISTINCT ON(item.id) * FROM thlemmde_household.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE NOT EXISTS(" +
            "SELECT 1 FROM thlemmde_household.interests interest " +
            "WHERE interest.user_id = :userId AND interest.item_id = item.id)" +
            " ORDER BY item.id DESC LIMIT 1", nativeQuery = true)
    Item findTopNotAssessedByUserId(Long userId);

    @Query(value="SELECT * FROM thlemmde_household.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " ORDER BY item.id DESC LIMIT 1", nativeQuery = true)
    Item findTopById();

    @Query(value="SELECT * FROM thlemmde_household.items item" +
            " INNER JOIN thlemmde_household.transactions transaction ON item.transaction_id = transaction.id" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE transaction.transaction_status_id = :transactionStatusId" +
            " GROUP BY item.id ORDER BY item.id DESC", nativeQuery = true)
    List<Item> findAllByTransactionStatusId(Long transactionStatusId);

    @Query(value="SELECT * FROM thlemmde_household.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE item_type.item_type_id = :itemTypeId" +
            " GROUP BY item.id ORDER BY item.id DESC", nativeQuery = true)
    List<Item> findAllByItemTypeId(Long itemTypeId);

    @Query(value="SELECT * FROM thlemmde_household.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE tag.tag IN :tags" +
            " GROUP BY item.id ORDER BY item.id DESC", nativeQuery = true)
    List<Item> findAllByTags(Set<String> tags);

    @Query(value="SELECT * FROM thlemmde_household.items item" +
            " INNER JOIN thlemmde_household.transactions transaction ON item.transaction_id = transaction.id" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE transaction.transaction_status_id = :transactionStatusId" +
            " AND item_type.item_type_id = :itemTypeId" +
            " GROUP BY item.id ORDER BY item.id DESC", nativeQuery = true)
    List<Item> findAllByTransactionStatusIdAndItemTypeId(Long transactionStatusId, Long itemTypeId);

    @Query(value="SELECT * FROM thlemmde_household.items item" +
            " INNER JOIN thlemmde_household.transactions transaction ON item.transaction_id = transaction.id" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE transaction.transaction_status_id = :transactionStatusId" +
            " AND item_type.item_type_id = :itemTypeId" +
            " AND tag.tag IN :tags" +
            " GROUP BY item.id ORDER BY item.id DESC", nativeQuery = true)
    List<Item> findAllByTransactionStatusIdAndItemTypeIdAndTags(Long transactionStatusId, Long itemTypeId, Set<String> tags);

    @Query(value="SELECT * FROM thlemmde_household.items item" +
            " INNER JOIN thlemmde_household.transactions transaction ON item.transaction_id = transaction.id" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE transaction.transaction_status_id = :transactionStatusId" +
            " AND tag.tag IN :tags" +
            " GROUP BY item.id ORDER BY item.id DESC", nativeQuery = true)
    List<Item> findAllByTransactionStatusIdAndTags(Long transactionStatusId, Set<String> tags);

    @Query(value="SELECT * FROM thlemmde_household.items item" +
            JOIN_NECESSARY_FIELDS_FOR_MAPPING +
            " WHERE item_type.item_type_id = :itemTypeId" +
            " AND tag.tag IN :tags" +
            " GROUP BY item.id ORDER BY item.id DESC", nativeQuery = true)
    List<Item> findAllByItemTypeIdAndTags(Long itemTypeId, Set<String> tags);
}
