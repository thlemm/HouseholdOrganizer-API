package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.model.Interest;
import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.User;

public interface InterestService {
    void createNewInterest(User user, Item item, boolean isInterested);
    void updateInterest(Interest interest, boolean isInterested);
}
