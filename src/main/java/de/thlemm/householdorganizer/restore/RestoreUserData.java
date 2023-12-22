package de.thlemm.householdorganizer.restore;

import de.thlemm.householdorganizer.model.UserRole;
import de.thlemm.householdorganizer.model.UserStatus;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class RestoreUserData {
    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9ÄäÖöÜü.\\-_]{3,20}$")
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,40}$")
    private String password;

    private Set<UserRole> userRoles = new HashSet<>();
    private UserStatus userStatus = new UserStatus();
}
