package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class SetUserRolesRequest {
    @NotNull
    private Long userId;
    private Set<Long> roleIds;
}
