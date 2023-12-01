package de.thlemm.householdorganizer.security.service;

import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.UserRoleRepository;
import de.thlemm.householdorganizer.repository.UserStatusRepository;
import de.thlemm.householdorganizer.repository.UserRepository;
import de.thlemm.householdorganizer.repository.UserTypeRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DefaultUsersService {
    @Value("${de.thlemm.householdorganizer.production:true}")
    private boolean production;

    @Value("${de.thlemm.householdorganizer.create-debug-user:false}")
    private boolean createDebugUser;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserStatusRepository userStatusRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    public static final String DEBUG_USERNAME = "debug",
                                DEBUG_PASSWORD = "Debug123",
                                DEBUG_EMAIL = "noreply@example.com";

    public static final Set<UserRoleName> DEBUG_ROLES = Set.of(UserRoleName.ROLE_USER, UserRoleName.ROLE_ADMIN);


    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    @Synchronized
    public void createUsersAfterStartup() {
        // Create the debug user if it doesn't exist yet
        User debugUser = userRepository.findByUsername(DEBUG_USERNAME);
        if(debugUser == null) {
            if(!createDebugUser || production) {
                // Debug user doesn't exist and shouldn't be created? Do nothing!
                return;
            }
            debugUser = new User(DEBUG_USERNAME, DEBUG_EMAIL, encoder.encode(DEBUG_PASSWORD));
        }
        // By default, the debug user can't login and doesn't have access to anything
        Set<UserRole> userRoles = Collections.emptySet();
        UserStatus userStatus = userStatusRepository.findByName(UserStatusName.USER_STATUS_BANNED);
        // But: if we're not in production mode, enable the debug user and give it all permissions
        if(!production) {
            userRoles = DEBUG_ROLES.stream()
                    .map(userRoleRepository::findByName)
                    .collect(Collectors.toSet());
            userStatus = userStatusRepository.findByName(UserStatusName.USER_STATUS_ACTIVE);
            log.warn("Debug user with full admin privileges is enabled. User: \"" + DEBUG_USERNAME
                    + "\", Password: \"" + DEBUG_PASSWORD + "\"");
        }
        UserType userType = userTypeRepository.findByName(UserTypeName.USER_TYPE_FAMILY);
        debugUser.setRoles(userRoles);
        debugUser.setUserStatus(userStatus);
        debugUser.setUserType(userType);
        userRepository.save(debugUser);
    }
}
