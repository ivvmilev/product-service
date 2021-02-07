package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ProductDto
{

    private Long id;

    @Length(min = 3, max = 100)
    @NotNull(message = "Product name is required.")
    private String name;

    @Length(min = 3, max = 200)
    private String category;

    @Length(min = 3, max = 500)
    private String description;

    private int quantity = 1;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate lastModifiedDate;
}
