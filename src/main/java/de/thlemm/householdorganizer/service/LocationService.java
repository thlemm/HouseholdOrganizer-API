package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.AddLocationRequest;
import de.thlemm.householdorganizer.model.Interest;
import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.User;

public interface LocationService {
    void createNewLocation(AddLocationRequest addLocationRequest);
    void updateCurrentRoomById(Long locationId, Long roomId);
}
