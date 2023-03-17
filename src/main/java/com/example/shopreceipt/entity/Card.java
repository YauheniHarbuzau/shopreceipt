package com.example.shopreceipt.entity;

import com.example.shopreceipt.entity.parent.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for the Card (database table - card)
 *
 * @see BasicEntity
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "card")
public class Card extends BasicEntity {

    /**
     * Fields, RU: Номер, Процент скидки
     */
    @NotNull
    @Positive
    @Size(min = 4, max = 16)
    @Column(name = "number", unique = true)
    private Integer number;

    @NotNull
    @Positive
    @Column(name = "discount")
    private Double discount;
}
