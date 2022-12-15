package com.example.product.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Data
public class Order
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product id cannot be empty")
    private Long productId;

    @Column(name = "product")
    @NotNull(message = "Product name cannot be empty")
    private String product;

    @NotNull(message = "Quantity should be at least 1")
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "created_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Column(name = "last_modified_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate lastModifiedDate;
}
