package de.thlemm.householdorganizer.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user_roles", schema="household_organizer")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private UserRoleName name;
}
