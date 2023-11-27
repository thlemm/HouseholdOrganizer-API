package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.controller.request.SearchItemsRequest;
import de.thlemm.householdorganizer.model.Item;

import java.util.List;

public interface ItemService {
    void createNewItem(AddItemRequest addItemRequest);
    void updateLocationById(Long itemId, Long locationId);
    List<Item> findAllBySearchRequest(SearchItemsRequest searchItemsRequest);
}
