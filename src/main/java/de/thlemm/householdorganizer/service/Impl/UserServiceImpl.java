package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.controller.request.SignupRequest;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.InterestRepository;
import de.thlemm.householdorganizer.repository.RoleRepository;
import de.thlemm.householdorganizer.repository.StatusRepository;
import de.thlemm.householdorganizer.repository.UserRepository;
import de.thlemm.householdorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    InterestRepository interestRepository;

    @Override
    public void createNewUser(SignupRequest signupRequest) {

        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER);
        roles.add(userRole);

        Status status = statusRepository.findByName(StatusName.STATUS_ACTIVE);

        user.setRoles(roles);
        user.setStatus(status);
        userRepository.save(user);
    }
}
