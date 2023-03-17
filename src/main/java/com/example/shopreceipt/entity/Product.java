package com.example.shopreceipt.entity;

import com.example.shopreceipt.entity.parent.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for the Product (database table - product)
 *
 * @see BasicEntity
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends BasicEntity {

    /**
     * Fields, RU: Наименование, Цена, Акция
     */
    @NotNull
    @Pattern(regexp = "^[A-Za-zА-Яа-я\\d]+$")
    @Column(name = "name")
    private String name;

    @NotNull
    @Positive
    @Column(name = "price")
    private Double price;

    @NotNull
    @Column(name = "promotion")
    private Boolean promotion;
}
