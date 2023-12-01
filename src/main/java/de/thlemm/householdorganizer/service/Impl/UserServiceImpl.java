package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.controller.request.SignupRequest;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.InterestRepository;
import de.thlemm.householdorganizer.repository.UserRoleRepository;
import de.thlemm.householdorganizer.repository.UserStatusRepository;
import de.thlemm.householdorganizer.repository.UserRepository;
import de.thlemm.householdorganizer.restore.RestoreUserData;
import de.thlemm.householdorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserStatusRepository userStatusRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    InterestRepository interestRepository;

    @Override
    public void createNewUser(SignupRequest signupRequest) {

        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = userRoleRepository.findByName(UserRoleName.ROLE_USER);
        userRoles.add(userRole);

        UserStatus userStatus = userStatusRepository.findByName(UserStatusName.USER_STATUS_ACTIVE);

        user.setRoles(userRoles);
        user.setUserStatus(userStatus);
        userRepository.save(user);
    }

    @Override
    public void restoreUser(RestoreUserData restoreUserData) {
        User user = new User(restoreUserData.getUsername(),
                restoreUserData.getEmail(),
                encoder.encode(restoreUserData.getPassword()));

        user.setRoles(restoreUserData.getUserRoles());
        user.setUserStatus(restoreUserData.getUserStatus());
        user.setUserType(restoreUserData.getUserType());

        userRepository.save(user);
    }
}
