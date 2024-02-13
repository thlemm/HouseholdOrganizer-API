package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.controller.request.SearchItemsRequest;
import de.thlemm.householdorganizer.controller.request.SetTransactionRequest;
import de.thlemm.householdorganizer.controller.resposnse.AddItemResponse;
import de.thlemm.householdorganizer.controller.resposnse.MessageResponse;
import de.thlemm.householdorganizer.model.*;

import de.thlemm.householdorganizer.repository.*;
import de.thlemm.householdorganizer.service.ItemService;
import de.thlemm.householdorganizer.service.TransactionService;
import de.thlemm.householdorganizer.service.exception.TransactionServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2")
public class ItemController {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ItemTypeRepository itemTypeRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionStatusRepository transactionStatusRepository;
    @Autowired
    TransactionService transactionService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items")
    public List<Item> getItems() {
        List<Item> itemList = itemRepository.findAll();
        return filterInterested(itemList);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items/user")
    public List<Item> getItemsOfUser(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        User authUser = userRepository.findByUsername(authentication.getName());

        List<Item> itemList = itemRepository.findAllOfInterestByUserId(authUser.getId());
        return filterInterested(itemList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{userId}/items")
    public ResponseEntity<?> getItemsOfUserById(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication,
            @PathVariable("userId") Long userId
    ) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Item> itemList = itemRepository.findAllOfInterestByUserId(user.getId());
        return ResponseEntity.ok(
                filterInterested(
                        itemList
                )
        );
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/item")
    public ResponseEntity<?> addItem(@Valid @RequestBody AddItemRequest addItemRequest) {

        if (!itemTypeRepository.existsById(addItemRequest.getType())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!roomRepository.existsById(addItemRequest.getOriginalRoom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!roomRepository.existsById(addItemRequest.getCurrentRoom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Item item = itemService.createNewItem(addItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AddItemResponse(
                        item.getId(),
                        item.getMark(),
                        item.getLocation()
                ));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/item/{itemId}/location/{locationId}")
    public ResponseEntity<?> updateLocation(
            @PathVariable("itemId") Long itemId,
            @PathVariable("locationId") Long locationId)
    {

        if (!itemRepository.existsById(itemId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!locationRepository.existsById(locationId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        itemService.updateLocationById(itemId, locationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/items/search")
    public ResponseEntity<?> searchItems(
            @Valid @RequestBody SearchItemsRequest searchItemsRequest) {

        if (searchItemsRequest.getMark() != null && !itemRepository.existsByMark(searchItemsRequest.getMark())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (searchItemsRequest.getType() != null && !itemTypeRepository.existsById(searchItemsRequest.getType())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        return ResponseEntity.ok(
                filterInterested(
                    itemService.findAllBySearchRequest(searchItemsRequest)
                )
        );
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items/location/{location}")
    public ResponseEntity<?> reverseSearchItems(@PathVariable("location") Long locationMark) {

        if (!locationRepository.existsByMark(locationMark)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Location location = locationRepository.findByMark(locationMark);

        if (!itemRepository.existsByLocation(location)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(
                filterInterested(
                    itemRepository.findAllByLocation(location)
                )
        );
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items/notassessed")
    public ResponseEntity<?> nextNotAssessedItem(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication)
    {
        User authUser = userRepository.findByUsername(authentication.getName());
        return ResponseEntity.ok(itemRepository.findTopNotAssessedByUserId(authUser.getId())
        );
    }

    @GetMapping("/item/today")
    public ResponseEntity<?> getItemOfTheDay() {
        return ResponseEntity.ok(
                itemService.getItemOfTheDay()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/item/{itemId}/transaction")
    public ResponseEntity<?> setTransactionForItem(
            @PathVariable("itemId") Long itemId,
            @Valid @RequestBody SetTransactionRequest setTransactionRequest
    ) {
        System.out.println("Request route successful");
        if (!itemRepository.existsById(itemId)) {
            System.out.println("Item id does not exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!transactionStatusRepository.existsById(setTransactionRequest.getTransactionStatusId())){
            System.out.println("Status id does not exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            System.out.println("Trying to set via service");
            transactionService.setTransaction(itemId, setTransactionRequest);
        } catch (TransactionServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMsg()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<Item> filterInterested(List<Item> itemList) {
        itemList.forEach(item ->
                item.setInterests(
                        item.getInterests().stream()
                                .filter(Interest::getInterested)
                                .collect(Collectors.toList())
                )
        );
        return itemList;
    }

}