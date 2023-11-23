package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.model.Room;
import de.thlemm.householdorganizer.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class RoomController {

    @Autowired
    RoomRepository roomRepository;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/rooms")
    public List<Room> getRooms(@RequestParam(value = "query", defaultValue="") String query) {

        return roomRepository.findAll();
    }
}
