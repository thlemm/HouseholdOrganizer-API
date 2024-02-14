package de.thlemm.householdorganizer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "locations", schema = "thlemmde_household")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mark;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "locations_rooms", schema = "thlemmde_household",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Room room;

    private Boolean box;
}
