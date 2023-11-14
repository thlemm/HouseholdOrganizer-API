package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.request.AddItemByAdminRequest;
import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.model.Item;

import de.thlemm.householdorganizer.repository.ItemRepository;
import de.thlemm.householdorganizer.repository.RoomRepository;
import de.thlemm.householdorganizer.repository.TypeRepository;
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

@RestController
@RequestMapping("/v1")
public class ItemController {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    ItemService itemService;

    @GetMapping("/items")
    public List<Item> getItems() {
        return itemRepository.findAll();
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

        itemService.createNewItem(addItemRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/item")
    public ResponseEntity<?> addItemByAdmin(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                     @Valid @RequestBody AddItemByAdminRequest addItemByAdminRequest) {

        if (itemRepository.existsById(addItemByAdminRequest.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!typeRepository.existsById(addItemByAdminRequest.getType())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!roomRepository.existsById(addItemByAdminRequest.getCurrentRoom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!roomRepository.existsById(addItemByAdminRequest.getOriginalRoom())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        itemService.createNewItemWithId(addItemByAdminRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
