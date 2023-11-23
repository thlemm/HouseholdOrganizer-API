package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.controller.request.SearchItemsRequest;
import de.thlemm.householdorganizer.model.Interest;
import de.thlemm.householdorganizer.model.Item;

import de.thlemm.householdorganizer.model.RoleName;
import de.thlemm.householdorganizer.model.User;
import de.thlemm.householdorganizer.repository.*;
import de.thlemm.householdorganizer.service.ItemService;
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
    TypeRepository typeRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

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

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/item")
    public ResponseEntity<?> addItem(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @Valid @RequestBody AddItemRequest addItemRequest) {

        if (!typeRepository.existsById(addItemRequest.getType())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!roomRepository.existsById(addItemRequest.getCurrentRoom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!roomRepository.existsById(addItemRequest.getOriginalRoom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (itemRepository.existsByMark(addItemRequest.getMark())) {
            System.out.println("Id already exists");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User authUser = userRepository.findByUsername(authentication.getName());
        boolean userIsAdmin = authUser.getRoles().contains(roleRepository.findByName(RoleName.ROLE_ADMIN));
        if (addItemRequest.getMark() != null && !userIsAdmin) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (addItemRequest.getCreated() != null && !userIsAdmin) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        itemService.createNewItem(addItemRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/item/{itemId}/location/{location}")
    public ResponseEntity<?> updateLocation(@PathVariable("itemId") Long itemId, @PathVariable("location") Long location) {

        if (!itemRepository.existsById(itemId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        itemService.updateLocationById(itemId, location);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/item/{itemId}/room/{roomId}")
    public ResponseEntity<?> updateCurrentRomm(@PathVariable("itemId") Long itemId, @PathVariable("roomId") Long roomId) {

        if (!itemRepository.existsById(itemId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!roomRepository.existsById(roomId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        itemService.updateCurrentRoomById(itemId, roomId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/items/search")
    public ResponseEntity<?> searchItems(
            @Valid @RequestBody SearchItemsRequest searchItemsRequest) {

        if (searchItemsRequest.getMark() != null && !itemRepository.existsByMark(searchItemsRequest.getMark())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (searchItemsRequest.getType() != null && !typeRepository.existsById(searchItemsRequest.getType())) {
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
    public ResponseEntity<?> reverseSearchItems(@PathVariable("location") Long location) {

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
    public ResponseEntity<?> nextNotAssessedItem(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        User authUser = userRepository.findByUsername(authentication.getName());
        return ResponseEntity.ok(itemRepository.findTopNotAssessedByUserId(authUser.getId())
        );
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