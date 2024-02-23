package de.thlemm.householdorganizer.restore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.*;
import de.thlemm.householdorganizer.service.ItemService;
import de.thlemm.householdorganizer.service.UserService;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RestoreDataService {
    @Value("${de.thlemm.householdorganizer.production:true}")
    private boolean production;
    @Value("${de.thlemm.householdorganizer.restore:false}")
    private boolean restore;

    private static final Logger logger = LoggerFactory.getLogger(RestoreDataService.class);

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    UserStatusRepository userStatusRepository;
    @Autowired
    TransactionStatusRepository transactionStatusRepository;
    @Autowired
    TransactionRepository transactionRepository;
    ApplicationContext context;

    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    @Synchronized
    public void restoreData() {

        logger.info("Checking database for existing items.");

        if (!itemRepository.findAll().isEmpty()) {
            logger.info("Database already has items. No restore procedure initiated.");
            return;
        }

        if (restore && !production) {
            logger.info("Restore procedure initiated...");
            createUsers();

            try {
                ObjectMapper mapper = new ObjectMapper();

                List<Map<String, Object>> map = mapper.readValue(new File("src/main/resources/data/2023-11-13_database_backup.json"), new TypeReference<List<Map<String, Object>>>(){});

                for (Map<String, Object> item : map) {

                    logger.debug("Restoring item with id: " + item.get("thing_id"));

                    Long boxId = Long.parseLong((String) item.get("box_id"));
                    if (!locationRepository.existsByMark(boxId)) {
                        logger.debug("Creating location with mark: " + boxId);
                        Location location = new Location();
                        location.setBox(boxId < 50);
                        location.setMark(boxId);
                        location.setRoom(
                                roomRepository.findById(
                                        getRoomIdFromRoomName((String) item.get("location")))
                        );
                        logger.debug("Location has room name: " + item.get("location"));
                        locationRepository.save(location);
                    }

                    RestoreItemData restoreItemData = new RestoreItemData();
                    restoreItemData.setMark(Long.parseLong((String) item.get("thing_id")));
                    restoreItemData.setImage((String) item.get("picture"));
                    restoreItemData.setLocation(locationRepository.findByMark(boxId).getId());
                    restoreItemData.setOriginalRoom(getRoomIdFromRoomName((String) item.get("room")));
                    restoreItemData.setCurrentRoom(getRoomIdFromRoomName((String) item.get("location")));
                    restoreItemData.setType(getTypeIdFromTypeName((String) item.get("type")));
                    restoreItemData.setCreated(item.get("timestamp") + "+02");
                    restoreItemData.setTags(
                            Arrays.stream(
                                            ((String) item.get("tags"))
                                                    .split(","))
                                    .collect(Collectors.toSet())
                    );
                    if (!itemRepository.existsByMark(restoreItemData.getMark())) {
                        itemService.restoreItem(restoreItemData);
                    }
                    List<String> users = RestoreVariables.NEW_USERS;
                    boolean rated = true;
                    for (String user : users) {
                        Interest interest = new Interest();
                        interest.setUser(
                                userRepository.findByUsername(user)
                        );
                        interest.setItem(
                                itemRepository.findByMark(Long.parseLong((String) item.get("thing_id")))
                        );

                        String oldUser;
                        if (Objects.equals(user, RestoreVariables.ALTERED_USER_NEW_A)) {
                            oldUser = RestoreVariables.ALTERED_USER_OLD_A;
                        } else if (Objects.equals(user, RestoreVariables.ALTERED_USER_NEW_B)) {
                            oldUser = RestoreVariables.ALTERED_USER_OLD_B;
                        } else {
                            oldUser = user;
                        }
                        long val = Long.parseLong((String) item.get(oldUser));
                        if (val < 2) {
                            if (!interestRepository.existsByUserAndItemAndInterestedTrue(interest.getUser(), interest.getItem())) {
                                interest.setInterested(val == 1);
                                interestRepository.save(interest);
                            }
                        } else {
                            rated = false;
                        }
                    }
                    boolean hasInterest = interestRepository.existsByItemAndInterestedTrue(
                            itemRepository.findByMark(Long.parseLong((String) item.get("thing_id")))
                    );

                    Transaction transaction = new Transaction();
                    TransactionStatus transactionStatus;
                    if (rated && hasInterest) {
                        transactionStatus = transactionStatusRepository.findByName(
                                TransactionStatusName.TRANSACTION_STATUS_RESERVED
                        );
                    } else if (rated) {
                        transactionStatus = transactionStatusRepository.findByName(
                                TransactionStatusName.TRANSACTION_STATUS_AVAILABLE
                        );
                    } else {
                        transactionStatus = transactionStatusRepository.findByName(
                                TransactionStatusName.TRANSACTION_STATUS_NOT_ASSESSED
                        );
                    }

                    transaction.setTransactionStatus(transactionStatus);
                    transaction.setUpdated(
                            OffsetDateTime.now(ZoneOffset.UTC)
                                    .truncatedTo(ChronoUnit.SECONDS)
                    );

                    transactionRepository.save(transaction);

                    Item updatedItem = itemRepository.findByMark(Long.parseLong((String) item.get("thing_id")));
                    updatedItem.setTransaction(transaction);

                    itemRepository.save(updatedItem);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("Restore procedure completed.");
    }

    private void createUsers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> users = mapper.readValue(new File("src/main/resources/data/user_dummy.json"), new TypeReference<List<Map<String, Object>>>() {
            });

            for (Map<String, Object> user : users) {
                RestoreUserData restoreUserData = new RestoreUserData();
                restoreUserData.setUsername((String) user.get("username"));
                restoreUserData.setPassword((String) user.get("password"));
                restoreUserData.setEmail((String) user.get("email"));

                Set<UserRole> userRoles = new HashSet<>(
                        Arrays.asList(
                                userRoleRepository.findByName(UserRoleName.ROLE_USER),
                                userRoleRepository.findByName(UserRoleName.ROLE_FAMILY)
                        )
                );
                if (restoreUserData.getUsername().equals(RestoreVariables.FORMER_ADMIN_USER)) {
                    userRoles.add(userRoleRepository.findByName(UserRoleName.ROLE_ADMIN));
                }

                restoreUserData.setUserRoles(userRoles);
                restoreUserData.setUserStatus(
                        userStatusRepository.findByName(UserStatusName.USER_STATUS_ACTIVE)
                );

                if(!userRepository.existsByUsername(restoreUserData.getUsername())) {
                    userService.restoreUser(restoreUserData);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Long getRoomIdFromRoomName(String roomName) {
        return switch (roomName) {
            case "Esszimmer" -> 1L;
            case "Wohnzimmer" -> 2L;
            case "Kueche" -> 3L;
            case "Hof" -> 4L;
            case "Arbeitszimmer" -> 5L;
            case "Schlafzimmer" -> 6L;
            case "Gruenes Zimmer" -> 7L;
            case "Lottozimmer" -> 8L;
            case "Liborizimmer I" -> 9L;
            case "Liborizimmer II" -> 10L;
            case "Bad" -> 11L;
            case "Toilette rosa" -> 12L;
            case "Toilette beige" -> 13L;
            case "Topfkammer" -> 14L;
            case "Diele EG" -> 15L;
            case "Diele 1. OG" -> 16L;
            case "Diele 2. OG" -> 17L;
            case "Keller" -> 20L;
            case "Suedbalkon" -> 22L;
            case "Nordbalkon" -> 23L;
            case "Nach Bild" -> 24L;
            default -> null;
        };
    }
    private Long getTypeIdFromTypeName(String typeName) {
        return switch (typeName) {
            case "Dekoration" -> 1L;
            case "Moebelstueck" -> 2L;
            case "Gebrauchsgegenstand" -> 3L;
            case "Technisches Geraet" -> 4L;
            case "Einrichtungsgegenstand" -> 5L;
            default -> null;
        };
    }
}
