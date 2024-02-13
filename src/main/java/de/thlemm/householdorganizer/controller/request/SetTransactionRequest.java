package de.thlemm.householdorganizer.controller.request;

import lombok.Data;

import java.util.Set;

@Data
public class SetTransactionRequest {
    private Long transactionStatusId;

    private Long priceMin;
    private Long priceMax;
    private Long priceSold;

    private Long userId;
    private Set<Long> interestedUsers;
}
