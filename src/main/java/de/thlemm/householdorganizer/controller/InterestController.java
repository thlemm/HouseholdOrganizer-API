package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.request.AddInterestRequest;
import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.*;
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
@RequestMapping("/v1")
public class InterestController {
    @Autowired
    InterestRepository interestRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @GetMapping("/interests")
    public List<Interest> getInterests() {
        return interestRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/interest")
    public ResponseEntity<?> addInterest(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                        @Valid @RequestBody AddInterestRequest addInterestRequest) {

        Interest interest = new Interest();
        User user = userRepository.findById(addInterestRequest.getUser());

        User authUser = userRepository.findByUsername(authentication.getName());
        boolean userIsAdmin = authUser.getRoles().contains(roleRepository.findByName(RoleName.ROLE_ADMIN));
        if (authUser != user && !userIsAdmin) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Item item = itemRepository.findById(addInterestRequest.getItem());
        if (item == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (interestRepository.findByUserAndItem(user, item) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        interest.setUser(user);
        interest.setItem(item);

        interestRepository.save(interest);

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
}
