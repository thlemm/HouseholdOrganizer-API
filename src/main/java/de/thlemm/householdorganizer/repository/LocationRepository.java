package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    boolean existsByMark(Long mark);
    Location findByMark(Long mark);
    Location findById(Long id);
    boolean existsById(Long id);
    List<Location> findAllByOrderByMarkAsc();
}
