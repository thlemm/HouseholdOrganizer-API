package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class SearchItemsRequest {
    private Long mark;
    private Long type;
    @NotNull
    private Set<String> tags;
}
