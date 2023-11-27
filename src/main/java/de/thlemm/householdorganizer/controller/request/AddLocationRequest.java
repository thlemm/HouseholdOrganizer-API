package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddLocationRequest {
    @NotNull
    private Long mark;
    @NotNull
    private Long room;
    @NotNull
    private Boolean box;
}
