package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    // @Query(value="", nativeQuery = true)
    // List<Tag> findAllStartsWith();
}
