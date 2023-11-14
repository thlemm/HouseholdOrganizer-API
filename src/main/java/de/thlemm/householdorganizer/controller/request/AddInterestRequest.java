package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class AddInterestRequest {
    private Long item;
    private Long user;
}
