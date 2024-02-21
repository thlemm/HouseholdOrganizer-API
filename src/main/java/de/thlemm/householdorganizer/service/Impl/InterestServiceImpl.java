package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.*;
import de.thlemm.householdorganizer.security.service.DefaultUsersService;
import de.thlemm.householdorganizer.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;


@Service
public class InterestServiceImpl implements InterestService {

    @Autowired
    InterestRepository interestRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionStatusRepository transactionStatusRepository;

    @Override
    public void createNewInterest(User user, Item item, boolean isInterested) {
        Interest interest = new Interest();
        interest.setUser(user);
        interest.setItem(item);
        interest.setInterested(isInterested);

        interestRepository.save(interest);

        updateTransactionStatusOfItem(item);
    }

    private void updateTransactionStatusOfItem(Item item) {
        Transaction transaction = item.getTransaction();
        transaction.setUpdated(
                OffsetDateTime.now(ZoneOffset.UTC)
                        .truncatedTo(ChronoUnit.SECONDS)
        );

        boolean itemIsAssessed = itemIsAssessed(item);
        boolean itemHasInterests = itemHasInterests(item);

        TransactionStatus transactionStatus;

        if (itemIsAssessed && itemHasInterests) {
            transactionStatus = transactionStatusRepository.findByName(
                    TransactionStatusName.TRANSACTION_STATUS_RESERVED
            );
        } else if (itemIsAssessed) {
            transactionStatus = transactionStatusRepository.findByName(
                    TransactionStatusName.TRANSACTION_STATUS_AVAILABLE
            );
        } else {
            transactionStatus = transactionStatusRepository.findByName(
                    TransactionStatusName.TRANSACTION_STATUS_NOT_ASSESSED
            );
        }
        transaction.setTransactionStatus(transactionStatus);
        transactionRepository.save(transaction);
    }

    private boolean itemIsAssessed(Item item) {
        List<User> users = userRepository.findAll();
        boolean isAssessed = true;
        for(User user: users) {
            if (!interestRepository.existsByItemAndUser(item, user)
                    && !Objects.equals(user.getUsername(), DefaultUsersService.DEBUG_USERNAME)) {
                isAssessed = false;
            }
        }
        return isAssessed;
    }

    private boolean itemHasInterests(Item item) {
        List<Interest> interests = interestRepository.findAllByItem(item);
        boolean hasInterests = false;
        for (Interest interest : interests) {
            if (interest.getInterested()) {
                hasInterests = true;
                break;
            }
        }
        return hasInterests;
    }

    @Override
    public void updateInterest(Interest interest, boolean isInterested) {
        interest.setInterested(isInterested);
        interestRepository.save(interest);
        updateTransactionStatusOfItem(interest.getItem());
    }

    @Override
    public void resetAllInterestsForItem(Item item){
        for(Interest interest : item.getInterests()) {
            interestRepository.delete(interest);
            updateTransactionStatusOfItem(item);
        }
    }
}
