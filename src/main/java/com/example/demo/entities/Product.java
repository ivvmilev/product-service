package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.NotFound;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "e_Products")
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
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @Column(name = "last_modified_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate lastModifiedDate;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = Math.max(quantity, 0);
    }

    public LocalDate getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate)
    {
        this.createdDate = createdDate;
    }

    public LocalDate getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }
}
