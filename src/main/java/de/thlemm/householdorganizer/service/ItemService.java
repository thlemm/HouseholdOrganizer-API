package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.controller.request.SearchItemsRequest;
import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.restore.RestoreItemData;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item createNewItem(AddItemRequest addItemRequest);
    void updateLocationById(Long itemId, Long locationId);
    List<Item> findAllBySearchRequest(SearchItemsRequest searchItemsRequest);
    void restoreItem(RestoreItemData restoreItemData);
    Item getItemOfTheDay();
}
