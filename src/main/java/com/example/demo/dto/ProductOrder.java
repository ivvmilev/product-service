package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductOrder
{
    private long productId;

    private long quantity;

    private String productName;
}
