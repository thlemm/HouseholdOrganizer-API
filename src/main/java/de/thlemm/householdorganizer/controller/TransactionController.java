package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.request.SetTransactionRequest;
import de.thlemm.householdorganizer.model.Transaction;
import de.thlemm.householdorganizer.repository.TransactionRepository;
import de.thlemm.householdorganizer.repository.TransactionStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2")
public class TransactionController {

    @Autowired
    TransactionStatusRepository transactionStatusRepository;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/transactions/states")
    public ResponseEntity<?> transactionStates() {
        return ResponseEntity.ok(
                transactionStatusRepository.findAll()
        );
    }
}
