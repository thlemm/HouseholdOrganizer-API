package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.request.AddLocationRequest;
import de.thlemm.householdorganizer.model.Location;
import de.thlemm.householdorganizer.model.Room;
import de.thlemm.householdorganizer.repository.LocationRepository;
import de.thlemm.householdorganizer.repository.RoomRepository;
import de.thlemm.householdorganizer.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class LocationController {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    LocationService locationService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/locations")
    public List<Location> getLocations() {

        return locationRepository.findAllByOrderByMarkAsc();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/location")
    public ResponseEntity<?> addNewLocation(
            @Valid @RequestBody AddLocationRequest addLocationRequest
            ) {

        if (locationRepository.existsByMark(addLocationRequest.getMark())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!roomRepository.existsById(addLocationRequest.getRoom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        locationService.createNewLocation(addLocationRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/location/{locationId}/room/{roomId}")
    public ResponseEntity<?> updateCurrentRoom(@PathVariable("locationId") Long locationId, @PathVariable("roomId") Long roomId) {

        if (!locationRepository.existsById(locationId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!roomRepository.existsById(roomId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        locationService.updateCurrentRoomById(locationId, roomId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/location/{mark}")
    public ResponseEntity<?> getBoxIfExists(@PathVariable("mark") Long mark) {
        Location location = locationRepository.findByMark(mark);
        if (location == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.ok(
                location
        );
    }

}
