package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.SetTransactionRequest;
import de.thlemm.householdorganizer.model.CasinoCode;
import de.thlemm.householdorganizer.service.exception.TransactionServiceException;

public interface CasinoCodeService {
    CasinoCode createNewCode();
}
