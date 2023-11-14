package de.thlemm.householdorganizer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "items", schema = "household_organizer")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "items_types", schema="household_organizer",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    private ItemType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "items_original_rooms", schema="household_organizer",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Room originalRoom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "items_current_rooms", schema="household_organizer",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Room currentRoom;

    @NotNull
    private Long location;

    @NotNull
    private String image;

    @NotNull
    private OffsetDateTime created;

    private OffsetDateTime updated;

    @JsonIgnoreProperties("item")
    @OneToMany(mappedBy = "item")
    private List<Tag> tags;

    @JsonIgnoreProperties("item")
    @OneToMany(mappedBy = "item")
    private List<Interest> interests;

}
