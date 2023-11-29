package de.thlemm.householdorganizer.restore;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;
@Data
public class RestoreItemData {
    private Long mark;
    private Long type;
    private Long originalRoom;
    private Long currentRoom;
    @NotNull
    private Long location;
    @NotBlank
    private String image;
    private Set<String> tags;
    @Pattern(regexp = "^[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}\\+[0-9]{2}$")
    private String created;
}
