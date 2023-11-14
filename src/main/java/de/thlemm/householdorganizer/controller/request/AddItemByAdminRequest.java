package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class AddItemByAdminRequest {
    Long id;
    private Long type;
    private Long originalRoom;
    private Long currentRoom;
    @NotBlank
    private String image;
    private Set<String> tags;
}
