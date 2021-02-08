package com.example.demo.quering;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCriteria
{
    private String field;
    private String operation;
    private Object value;
}
