package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.Room;
import de.thlemm.householdorganizer.model.Tag;
import de.thlemm.householdorganizer.repository.ItemRepository;
import de.thlemm.householdorganizer.repository.RoomRepository;
import de.thlemm.householdorganizer.repository.TagRepository;
import de.thlemm.householdorganizer.repository.TypeRepository;
import de.thlemm.householdorganizer.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TagRepository tagRepository;
    @Override
    public void createNewItem(AddItemRequest addItemRequest) {

        Item item = new Item();
        if (addItemRequest.getId() != null) {
            item.setId(addItemRequest.getId());
        }
        item.setType(typeRepository.findById(addItemRequest.getType()));
        item.setCurrentRoom(roomRepository.findById(addItemRequest.getCurrentRoom()));
        item.setOriginalRoom(roomRepository.findById(addItemRequest.getOriginalRoom()));
        item.setLocation(addItemRequest.getLocation());
        item.setImage(addItemRequest.getImage());
        item.setCreated(OffsetDateTime.now(ZoneOffset.UTC));

        itemRepository.save(item);

        for (String value : addItemRequest.getTags()) {
            Tag tag = new Tag();
            tag.setTag(value);
            tag.setItem(item);
            tagRepository.save(tag);
        }
    }

    @Override
    public void updateLocationById(Long itemId, Long location) {
        Item item = itemRepository.findById(itemId);
        item.setLocation(location);
        item.setUpdated(OffsetDateTime.now(ZoneOffset.UTC));
        itemRepository.save(item);
    }

    @Override
    public void updateCurrentRoomById(Long itemId, Long roomId) {
        Item item = itemRepository.findById(itemId);
        Room currentRoom = roomRepository.findById(roomId);
        item.setCurrentRoom(currentRoom);
        item.setUpdated(OffsetDateTime.now(ZoneOffset.UTC));
        itemRepository.save(item);
    }
}
