package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.controller.request.AddItemByAdminRequest;
import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.model.Item;
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
        item.setType(typeRepository.findById(addItemRequest.getType()));
        item.setCurrentRoom(roomRepository.findById(addItemRequest.getCurrentRoom()));
        item.setOriginalRoom(roomRepository.findById(addItemRequest.getOriginalRoom()));
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
    public void createNewItemWithId(AddItemByAdminRequest addItemByAdminRequest) {
        Item item = new Item();
        item.setId(addItemByAdminRequest.getId());
        item.setType(typeRepository.findById(addItemByAdminRequest.getType()));
        item.setCurrentRoom(roomRepository.findById(addItemByAdminRequest.getCurrentRoom()));
        item.setOriginalRoom(roomRepository.findById(addItemByAdminRequest.getOriginalRoom()));
        item.setImage(addItemByAdminRequest.getImage());
        item.setCreated(OffsetDateTime.now(ZoneOffset.UTC));

        itemRepository.save(item);

        for (String value : addItemByAdminRequest.getTags()) {
            Tag tag = new Tag();
            tag.setTag(value);
            tag.setItem(item);
            tagRepository.save(tag);
        }
    }
}
