package de.thlemm.householdorganizer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "items", schema = "thlemmde_household")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mark;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "items_types", schema = "thlemmde_household",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "item_type_id"))
    private ItemType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "items_original_rooms", schema = "thlemmde_household",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Room originalRoom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "items_locations", schema = "thlemmde_household",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    private Location location;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

}
