package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class GetRandomItemRequest {

    @Size(min = 6, max = 6)
    private String code;
}
