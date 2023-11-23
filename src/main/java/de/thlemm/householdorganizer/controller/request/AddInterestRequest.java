package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AddInterestRequest {
    @NotNull
    private Long item;
    private Long user;
    @NotNull
    private Boolean interested;
}
