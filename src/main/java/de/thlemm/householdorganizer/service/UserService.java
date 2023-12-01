package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.SignupRequest;
import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.User;
import de.thlemm.householdorganizer.restore.RestoreItemData;
import de.thlemm.householdorganizer.restore.RestoreUserData;

import java.util.List;

public interface UserService {
    void createNewUser(SignupRequest signupRequest);
    void restoreUser(RestoreUserData restoreUserData);
}
