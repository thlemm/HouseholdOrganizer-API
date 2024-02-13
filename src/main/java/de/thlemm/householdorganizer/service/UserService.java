package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.SignupRequest;
import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.User;
import de.thlemm.householdorganizer.model.UserRole;
import de.thlemm.householdorganizer.restore.RestoreItemData;
import de.thlemm.householdorganizer.restore.RestoreUserData;

import java.util.List;
import java.util.Set;

public interface UserService {
    void createNewUser(SignupRequest signupRequest);
    void restoreUser(RestoreUserData restoreUserData);
    void updateUserRoles(User user, Set<UserRole> newRoles);
}
