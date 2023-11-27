package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.controller.request.AddLocationRequest;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.*;
import de.thlemm.householdorganizer.service.InterestService;
import de.thlemm.householdorganizer.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    LocationRepository locationRepository;

    @Override
    public void createNewLocation(AddLocationRequest addLocationRequest) {
        Location location = new Location();
        location.setMark(addLocationRequest.getMark());
        location.setRoom(
                roomRepository.findById(addLocationRequest.getRoom())
        );
        location.setBox(addLocationRequest.getBox());
        locationRepository.save(location);
    }

    @Override
    public void updateCurrentRoomById(Long locationId, Long roomId) {
        Location location = locationRepository.findById(locationId);

        location.setRoom(roomRepository.findById(roomId));
        locationRepository.save(location);
    }

}
