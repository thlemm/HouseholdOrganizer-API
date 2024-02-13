package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.controller.request.SetTransactionRequest;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.*;
import de.thlemm.householdorganizer.service.InterestService;
import de.thlemm.householdorganizer.service.TransactionService;
import de.thlemm.householdorganizer.service.exception.TransactionServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionStatusRepository transactionStatusRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InterestService interestService;
    @Autowired
    InterestRepository interestRepository;

    @Override
    public void setTransaction(Long itemId, SetTransactionRequest setTransactionRequest) throws TransactionServiceException {

        Item item = itemRepository.findById(itemId);
        Transaction transaction = item.getTransaction();

        TransactionStatus newTransactionStatus = transactionStatusRepository.findById(
                setTransactionRequest.getTransactionStatusId()
        );
        transaction.setTransactionStatus(newTransactionStatus);
        transaction.setUpdated(
                OffsetDateTime.now(ZoneOffset.UTC)
                        .truncatedTo(ChronoUnit.SECONDS)
        );

        switch (newTransactionStatus.getName()) {
            case TRANSACTION_STATUS_AVAILABLE -> setTransactionStatusToAvailable(item, transaction, setTransactionRequest);
            case TRANSACTION_STATUS_SOLD -> setTransactionStatusToSold(transaction, setTransactionRequest);
            case TRANSACTION_STATUS_TAKEN -> setTransactionStatusToTaken(transaction, setTransactionRequest);
            case TRANSACTION_STATUS_RESERVED -> setTransactionStatusToReserved(item, setTransactionRequest);
            case TRANSACTION_STATUS_NOT_ASSESSED -> setTransactionStatusToNotAssessed(item);
        }
        transactionRepository.save(transaction);
        itemRepository.save(item);
    }

    private void setTransactionStatusToNotAssessed(Item item) {
        interestService.resetAllInterestsForItem(item);
        // ToDo: Update existing functions to serve item with status NOT_ASSESSED for assessment only.
    }
    private void setTransactionStatusToReserved(Item item, SetTransactionRequest setTransactionRequest) {
        for(Long userId : setTransactionRequest.getInterestedUsers()) {
            if (!userRepository.existsById(userId)) {
                throw new TransactionServiceException("At least one of the requested users does not exist.");
            }
        }
        for (Interest interest : item.getInterests()) {
            interestService.updateInterest(interest, false);
        }
        for (Long userId : setTransactionRequest.getInterestedUsers()) {
            User user = userRepository.findById(userId);
            if (interestRepository.existsByItemAndUser(
                    item,
                    user
            )) {
                Interest interest = interestRepository.findByUserAndItem(
                        user,
                        item
                );
                interestService.updateInterest(interest, true);
            } else {
                interestService.createNewInterest(
                        user,
                        item,
                        true
                );
            }

        }
    }
    private void setTransactionStatusToTaken(Transaction transaction, SetTransactionRequest setTransactionRequest) {
        if (setTransactionRequest.getUserId() == null) {
            throw new TransactionServiceException("No user was given.");
        }
        if (!userRepository.existsById(setTransactionRequest.getUserId())) {
            throw new TransactionServiceException("The requested user does not exist.");
        }
        transaction.setUser(
                userRepository.findById(setTransactionRequest.getUserId())
        );
    }
    private void setTransactionStatusToSold(Transaction transaction, SetTransactionRequest setTransactionRequest) {
        transaction.setPriceSold(setTransactionRequest.getPriceSold());
    }
    private void setTransactionStatusToAvailable(Item item, Transaction transaction, SetTransactionRequest setTransactionRequest) {
        // Removes interests from family members since item is available for non-family members now.
        // Other users such as friends can remain interested.
        for (Interest interest : item.getInterests()) {
            if(interest.getUser().getRoles().contains(userRoleRepository.findByName(UserRoleName.ROLE_FAMILY))) {
              interestService.updateInterest(interest, false);
            }
        }
        transaction.setPriceMin(setTransactionRequest.getPriceMin());
        transaction.setPriceMax(setTransactionRequest.getPriceMax());
    }
}
