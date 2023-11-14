package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.Room;
import de.thlemm.householdorganizer.model.RoomName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room findByName(RoomName name);
    Room findById(Long id);
    boolean existsById(Long id);
}
