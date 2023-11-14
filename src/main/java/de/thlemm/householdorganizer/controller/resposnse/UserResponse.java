package de.thlemm.householdorganizer.controller.resposnse;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
	private Long id;
	private String username;
	private String email;
	private final List<String> roles;

	public UserResponse(Long id, String username, String email, List<String> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}
}
