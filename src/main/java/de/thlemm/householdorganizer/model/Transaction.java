package de.thlemm.householdorganizer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Data
@Table(name = "transactions", schema = "household_organizer")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="price_min")
    private Long priceMin;
    @Column(name="price_max")
    private Long priceMax;
    @Column(name="price_sold")
    private Long priceSold;

    private OffsetDateTime updated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_status_id", referencedColumnName = "id")
    private TransactionStatus transactionStatus;

    @JsonIgnoreProperties({"interests", "password", "email", "roles", "status"})
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
