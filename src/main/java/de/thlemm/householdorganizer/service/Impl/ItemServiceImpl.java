package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.controller.request.AddItemRequest;
import de.thlemm.householdorganizer.controller.request.SearchItemsRequest;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.*;
import de.thlemm.householdorganizer.restore.RestoreItemData;
import de.thlemm.householdorganizer.service.ItemService;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ItemTypeRepository itemTypeRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    TransactionStatusRepository transactionStatusRepository;

    @Autowired
    TransactionRepository transactionRepository;

    private Long itemOfTheDay;

    @EventListener(ApplicationReadyEvent.class)
    @Order(3)
    @Synchronized
    private void setItemOfTheDayOnStartUp() {
        setItemOfTheDay();
    }
     @Scheduled(cron = "00 00 * * *", zone = "Europe/Paris")
     private void setItemOfTheDayOnSchedule() {
         setItemOfTheDay();
     }

     private void setItemOfTheDay() {
        Item topItem = itemRepository.findTopById();
        if (topItem != null) {
            Long topId = topItem.getId();
            while (true) {
                Long randomId = (long) (Math.random() * topId + 1);
                if (itemRepository.existsById(randomId)) {
                    itemOfTheDay = randomId;
                    break;
                }
            }
        }

     }

    @Override
    public Item createNewItem(AddItemRequest addItemRequest) {

        Item item = new Item();
        item.setMark(itemRepository.findTopByOrderByMarkDesc().getMark() + 1L);
        item.setCreated(OffsetDateTime.now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
        );

        item.setType(itemTypeRepository.findById(addItemRequest.getType()));

        Location location = locationRepository.findByMark(addItemRequest.getLocation());
        if (location == null) {
            location = new Location();
            location.setMark(addItemRequest.getLocation());
            location.setBox(addItemRequest.getBox());
            location.setRoom(roomRepository.findById(addItemRequest.getCurrentRoom()));
            locationRepository.save(location);
        }
        item.setLocation(location);

        item.setOriginalRoom(roomRepository.findById(addItemRequest.getOriginalRoom()));
        item.setImage(addItemRequest.getImage());

        Transaction transaction = new Transaction();
        transaction.setTransactionStatus(
                transactionStatusRepository.findByName(
                    TransactionStatusName.TRANSACTION_STATUS_NOT_ASSESSED
                )
        );
        transaction.setUpdated(
                OffsetDateTime.now(ZoneOffset.UTC)
                        .truncatedTo(ChronoUnit.SECONDS)
        );

        transactionRepository.save(transaction);
        item.setTransaction(transaction);

        itemRepository.save(item);

        for (String value : addItemRequest.getTags()) {
            Tag tag = new Tag();
            tag.setTag(value);
            tag.setItem(item);
            tagRepository.save(tag);
        }
        return item;
    }

    public void restoreItem(RestoreItemData restoreItemData) {
        Item item = new Item();
        if (restoreItemData.getMark() != null) {
            item.setMark(restoreItemData.getMark());
        } else {
            item.setMark(itemRepository.findTopByOrderByMarkDesc().getMark() + 1L);
        }
        if (restoreItemData.getCreated() != null) {
            item.setCreated(
                    OffsetDateTime.parse(
                            restoreItemData.getCreated(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx")
                    )
            );
        } else {
            item.setCreated(OffsetDateTime.now(ZoneOffset.UTC)
                    .truncatedTo(ChronoUnit.SECONDS)
            );
        }
        item.setType(itemTypeRepository.findById(restoreItemData.getType()));
        item.setLocation(locationRepository.findById(restoreItemData.getLocation()));
        item.setOriginalRoom(roomRepository.findById(restoreItemData.getOriginalRoom()));
        item.setImage(restoreItemData.getImage());

        itemRepository.save(item);

        for (String value : restoreItemData.getTags()) {
            Tag tag = new Tag();
            tag.setTag(value);
            tag.setItem(item);
            tagRepository.save(tag);
        }
    }

    @Override
    public void updateLocationById(Long itemId, Long locationId) {
        Item item = itemRepository.findById(itemId);
        item.setLocation(
                locationRepository.findById(locationId)
        );
        item.setUpdated(OffsetDateTime.now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
        );
        itemRepository.save(item);
    }

    @Override
    public List<Item> findAllBySearchRequest(SearchItemsRequest searchItemsRequest) {

        if (searchItemsRequest.getMark() != null) {
            return itemRepository.findAllByMark(searchItemsRequest.getMark());
        }

        List<Item> items = itemRepository.findAll();
        if (searchItemsRequest.getType() != null) {
            ItemTypeName itemTypeName = itemTypeRepository.findById(searchItemsRequest.getType()).getName();
            items = items.stream()
                    .filter(item -> item.getType() != null && Objects.equals(item.getType().getName(), itemTypeName))
                    .collect(Collectors.toList());
        }
        if (searchItemsRequest.getStatus() != null) {
            TransactionStatusName transactionStatusName = transactionStatusRepository.findById(searchItemsRequest.getStatus()).getName();
            items = items.stream()
                    .filter(item -> item.getTransaction().getTransactionStatus().getName().equals(transactionStatusName))
                    .collect(Collectors.toList());
        }
        if (searchItemsRequest.getTags() != null) {
            if (searchItemsRequest.getTags().size() > 0) {
                List<String> lowerCaseSearchTags = searchItemsRequest.getTags().stream()
                        .map(String::toLowerCase).toList();

                items = items.stream()
                        .filter(item -> item.getTags().stream()
                                .anyMatch(tag -> lowerCaseSearchTags.contains(tag.getTag().toLowerCase())))
                        .collect(Collectors.toList());
            }
        }
        return items;

    }

    @Override
    public Item getItemOfTheDay() {
        return itemRepository.findById(itemOfTheDay);
    }

    @Override
    public List<Item> findAllByTransactionStatus(TransactionStatusName transactionStatusName) {
        TransactionStatus transactionStatus = transactionStatusRepository.findByName(transactionStatusName);

        List<Item> items = itemRepository.findAll();
        items.removeIf(item -> !Objects.equals(item.getTransaction().getTransactionStatus(), transactionStatus));

        return items;
    }

    @Override
    public Item getCasinoItem() {
        List<Item> availableItems = findAllByTransactionStatus(TransactionStatusName.TRANSACTION_STATUS_AVAILABLE);
        Random random = new Random();
        int index = random.nextInt(availableItems.size());
        return availableItems.get(index);
    }
}
