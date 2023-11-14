package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.AddItemRequest;

public interface ItemService {
    void createNewItem(AddItemRequest addItemRequest);
    void updateLocationById(Long itemId, Long location);

    void updateCurrentRoomById(Long itemId, Long roomId);
}
