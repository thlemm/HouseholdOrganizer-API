package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.request.LoginRequest;
import de.thlemm.householdorganizer.controller.request.SetUserRolesRequest;
import de.thlemm.householdorganizer.controller.request.SignupRequest;
import de.thlemm.householdorganizer.controller.response.JwtResponse;
import de.thlemm.householdorganizer.controller.response.MessageResponse;
import de.thlemm.householdorganizer.controller.response.UserResponse;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.UserRepository;
import de.thlemm.householdorganizer.repository.UserRoleRepository;
import de.thlemm.householdorganizer.security.jwt.JwtUtils;
import de.thlemm.householdorganizer.security.service.UserDetailsImpl;
import de.thlemm.householdorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    JwtUtils jwtUtils;


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        User user = userRepository.findByUsername(loginRequest.getUsername());
        try {
            if (user.getUserStatus().getName() == UserStatusName.USER_STATUS_BANNED) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: User is banned from the server!"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Status request failed"));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        userService.createNewUser(signUpRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(
              userRoleRepository.findAll()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(
                userRepository.findAll()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/user/roles")
    public ResponseEntity<?> setUserRoles(@Valid @RequestBody SetUserRolesRequest setUserRolesRequest) {
        User user = userRepository.findById(setUserRolesRequest.getUserId());
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist!"));
        }

        Set<UserRole> newRoles = new HashSet<>();
        for (Long roleId : setUserRolesRequest.getRoleIds()) {
            UserRole userRole = userRoleRepository.findById(roleId);
            if (userRole == null) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Role does not exist!"));
            }
            newRoles.add(userRole);
        }

        userService.updateUserRoles(user, newRoles);

        return ResponseEntity.ok(new MessageResponse("User roles updated successfully!"));
    }
}
