package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
@Data
public class AddItemRequest {
    private Long id;
    private Long type;
    private Long originalRoom;
    private Long currentRoom;
    @NotNull
    private Long location;
    @NotBlank
    private String image;
    private Set<String> tags;
}
