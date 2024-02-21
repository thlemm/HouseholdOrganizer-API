package de.thlemm.householdorganizer.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "casino_codes", schema = "thlemmde_household")
public class CasinoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
}
