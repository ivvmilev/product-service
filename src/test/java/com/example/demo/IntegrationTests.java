package com.example.demo;

import com.example.demo.dto.ProductDto;
import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IntegrationTests
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    EntityManager em;


    @AfterEach
    private void flushData()
    {
        em.flush();
    }

    @Test
    void saveProductTest() throws Exception
    {
        String name = "Laptop Lenovo";

        saveProduct(name);

        Product productEntity = productRepository.findProductByName(name).orElse(null);

        assertThat(Objects.requireNonNull(productEntity).getName()).isEqualTo(name);
    }

    private ProductDto saveProduct(String name) throws Exception
    {
        ProductDto product = new ProductDto();
        product.setName(name);
        product.setQuantity(155);
        product.setCreatedDate(LocalDate.now());
        ResultActions resultActions = mockMvc.perform(post("/product", product)
                .flashAttr("user", product)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, ProductDto.class);
    }

    @Test
    public void deleteProductTest()
            throws Exception
    {
        String name = "Laptop Lenovo";

        ProductDto savedProduct = saveProduct(name);

        mockMvc.perform(delete("/product/delete/{id}", savedProduct.getId())
                .contentType("application/json"));

        Optional<ProductDto> productAfterDelete = Optional.ofNullable(getProductById(savedProduct.getId()));

        assert (productAfterDelete.isEmpty());
    }

    private ProductDto getProductById(long id) throws Exception
    {
        ResultActions resultActions = mockMvc.perform(get("/product/{id}", id)
                .contentType("application/json"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        if (!contentAsString.isEmpty())
        {
            return objectMapper.readValue(contentAsString, ProductDto.class);
        } else
        {
            return null;
        }
    }
}
