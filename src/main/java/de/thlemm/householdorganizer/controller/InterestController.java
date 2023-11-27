package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.request.AddInterestRequest;
import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.*;
import de.thlemm.householdorganizer.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class InterestController {
    @Autowired
    InterestRepository interestRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    InterestService interestService;


    @GetMapping("/interests")
    public List<Interest> getInterests() {
        return interestRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/interest")
    public ResponseEntity<?> addInterest(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                        @Valid @RequestBody AddInterestRequest addInterestRequest) {

        Interest interest = new Interest();
        User authUser = userRepository.findByUsername(authentication.getName());
        User user = userRepository.findById(addInterestRequest.getUser());
        if (addInterestRequest.getUser() == null) {
            user = authUser;
        }

        boolean userIsAdmin = authUser.getRoles().contains(roleRepository.findByName(RoleName.ROLE_ADMIN));
        if (user != authUser && !userIsAdmin) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Item item = itemRepository.findById(addInterestRequest.getItem());
        if (item == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (interestRepository.findByUserAndItem(user, item) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        interestService.createNewInterest(
                authUser,
                item,
                addInterestRequest.getInterested()
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/interest/{id}")
    public ResponseEntity<?> deleteInterest(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                            @PathVariable("id") Long id) {
        Interest interest = interestRepository.findById(id);
        if (interest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User authUser = userRepository.findByUsername(authentication.getName());
        boolean userIsAdmin = authUser.getRoles().contains(roleRepository.findByName(RoleName.ROLE_ADMIN));
        if (authUser != interest.getUser() && !userIsAdmin) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        interestRepository.delete(interest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/interest/item/{itemId}")
            public ResponseEntity<?> updateInterest(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                                    @PathVariable("itemId") Long itemId,
                                                    @RequestParam Boolean isInterested) {

        if (!itemRepository.existsById(itemId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User authUser = userRepository.findByUsername(authentication.getName());
        boolean userIsAdmin = authUser.getRoles().contains(roleRepository.findByName(RoleName.ROLE_ADMIN));

        if (interestRepository.existsByItemAndUser(
                itemRepository.findById(itemId),
                authUser
        )) {
            Interest interest = interestRepository.findByUserAndItem(
                    authUser,
                    itemRepository.findById(itemId)
            );
            if (authUser != interest.getUser() && !userIsAdmin) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            interestService.updateInterest(
                    interest,
                    isInterested);
        } else {
            interestService.createNewInterest(
                    authUser,
                    itemRepository.findById(itemId),
                    isInterested
            );
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
