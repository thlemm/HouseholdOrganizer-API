package de.thlemm.householdorganizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users", schema = "thlemmde_household")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false, unique = true)
    private String username;
    @JsonIgnore
    @NotNull
    @Column(nullable = false, unique = false)
    private String password;
    @JsonIgnore
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", schema = "thlemmde_household",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_role_id"))
    private Set<UserRole> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "users_status", schema = "thlemmde_household",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_status_id"))
    private UserStatus userStatus = new UserStatus();

    @JsonIgnoreProperties("user")
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Interest> interests;

    public User () {}

    public User(String username, String email, String password)
    {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}