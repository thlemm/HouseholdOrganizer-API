package de.thlemm.householdorganizer.controller.resposnse;

import de.thlemm.householdorganizer.model.Location;
import lombok.Data;

@Data
public class AddItemResponse {

    private Long id;
    private Long mark;
    private Location location;
    public AddItemResponse(Long id, Long mark, Location location) {
        this.id = id;
        this.mark = mark;
        this.location = location;
    }
}
