package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.controller.request.SetTransactionRequest;
import de.thlemm.householdorganizer.model.Transaction;
import de.thlemm.householdorganizer.service.exception.TransactionServiceException;

public interface TransactionService {
    void setTransaction(Long itemId, SetTransactionRequest setTransactionRequest) throws TransactionServiceException;
}
