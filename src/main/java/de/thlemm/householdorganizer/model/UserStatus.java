package de.thlemm.householdorganizer.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "user_status", schema="household_organizer")
public class UserStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private UserStatusName name;
}
