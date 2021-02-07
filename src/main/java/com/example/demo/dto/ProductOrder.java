package com.example.demo.dto;

import lombok.Data;

@Data
public class ProductOrder
{
    private long productId;

    private long quantity;

    private String productName;

    public ProductOrder(long id, int quantity, String productName)
    {
        this.productId = id;
        this.quantity = quantity;
        this.productName = productName;
    }
}
