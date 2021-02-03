package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
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

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String productName)
    {
        this.product = productName;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
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
