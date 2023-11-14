package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.AddItemByAdminRequest;
import de.thlemm.householdorganizer.controller.request.AddItemRequest;

public interface ItemService {
    void createNewItem(AddItemRequest addItemRequest);
    void createNewItemWithId(AddItemByAdminRequest addItemByAdminRequest);
}
