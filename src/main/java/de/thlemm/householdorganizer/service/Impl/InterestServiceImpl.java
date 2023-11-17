package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.model.Interest;
import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.User;
import de.thlemm.householdorganizer.repository.InterestRepository;
import de.thlemm.householdorganizer.repository.ItemRepository;
import de.thlemm.householdorganizer.repository.UserRepository;
import de.thlemm.householdorganizer.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InterestServiceImpl implements InterestService {

    @Autowired
    InterestRepository interestRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void createNewInterest(User user, Item item) {
        Interest interest = new Interest();
        interest.setUser(user);
        interest.setItem(item);

        interestRepository.save(interest);

        if (isItemIsAssessed(item)) {
            item.setAssessed(true);
            itemRepository.save(item);
        }
    }

    private boolean isItemIsAssessed(Item item) {
        List<User> users = userRepository.findAll();
        boolean isAssessed = true;
        for(User user: users) {
            if (!interestRepository.existsByUserAndItem(user, item)) {
                isAssessed = false;
            }
        }
        return isAssessed;
    }
}
