package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.controller.request.SearchItemsRequest;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        if (addItemRequest.getCreated() != null) {
            item.setCreated(
                    OffsetDateTime.parse(
                            addItemRequest.getCreated(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx")
                    )
            );
        } else {
            item.setCreated(OffsetDateTime.now(ZoneOffset.UTC)
                    .truncatedTo(ChronoUnit.SECONDS)
            );
        }
        item.setType(typeRepository.findById(addItemRequest.getType()));
        item.setCurrentRoom(roomRepository.findById(addItemRequest.getCurrentRoom()));
        item.setOriginalRoom(roomRepository.findById(addItemRequest.getOriginalRoom()));
        item.setLocation(addItemRequest.getLocation());
        item.setImage(addItemRequest.getImage());

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
        item.setUpdated(OffsetDateTime.now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
        );
        itemRepository.save(item);
    }

    @Override
    public void updateCurrentRoomById(Long itemId, Long roomId) {
        Item item = itemRepository.findById(itemId);
        Room currentRoom = roomRepository.findById(roomId);
        item.setCurrentRoom(currentRoom);
        item.setUpdated(OffsetDateTime.now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
        );
        itemRepository.save(item);
    }

    @Override
    public List<Item> findAllBySearchRequest(SearchItemsRequest searchItemsRequest) {

        boolean requestHasType = searchItemsRequest.getType() != null;
        boolean requestHasTags = searchItemsRequest.getTags().size() > 0;

        if (requestHasTags && !requestHasType) {
            String tags = String.join(",", searchItemsRequest.getTags());
            return itemRepository.findAllByTags(
                    tags
            );
        } else if (!requestHasTags && requestHasType) {
            return itemRepository.findAllByType(
                    searchItemsRequest.getType()
            );
        } else if (requestHasTags && requestHasType) {
            String tags = String.join(",", searchItemsRequest.getTags());
            return itemRepository.findAllByTypeAndTags(
                    searchItemsRequest.getType(),
                    tags
            );
        } else {
            return null;
        }
    }
}
