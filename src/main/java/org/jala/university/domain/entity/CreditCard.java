package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.jala.university.commons.domain.entity.BaseEntity;
import org.jala.university.domain.enums.CreditCardStatus;
import org.jala.university.domain.enums.TargetType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.YearMonth;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@Getter
@AllArgsConstructor
@Table(name = "credit_cards")
public final class CreditCard implements BaseEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID credit_card_id;

    @Column(name = "card_number", unique = true, nullable = false)
    private int cardNumber;

    @Column(name = "mask_card_number", nullable = false)
    private String maskCardNumber;

    @Column(nullable = false)
    private YearMonth expiration;

    @Column
    private Double limitAmount;

    @Column
    private TargetType targetType;

    @Column(nullable = false, length = 3)
    private Integer cvc;

    @Column(name = "password_hash", nullable = false, length = 4)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private CreditCardStatus status;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public UUID getId() {
        return credit_card_id;
    }

    public CreditCard() {
        super();
    }

}


