package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9ÄäÖöÜü.\\-_]{3,20}$")
    private String username;
 
    @NotBlank
    @Size(min = 6, max = 40)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,40}$")
    private String password;
}
