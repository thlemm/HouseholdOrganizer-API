package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.SignupRequest;

public interface UserService {
    void createNewUser(SignupRequest signupRequest);
}
