package com.example.product.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "Products")
@Data
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 100)
    @Column(name = "name")
    @NotNull(message = "Product name is required.")
    private String name;

    @Length(min = 3, max = 200)
    @Column(name = "category")
    private String category;

    @Length(min = 3, max = 500)
    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private int quantity = 0;

    @Column(name = "created_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;

    @Column(name = "last_modified_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastModifiedDate;
}
