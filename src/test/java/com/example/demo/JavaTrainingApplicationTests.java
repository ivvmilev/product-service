package com.example.demo;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.ProductService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@SpringBootTest
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JavaTrainingApplicationTests
{

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public ProductService productService() {
            return new ProductService();
        }
    }

    @BeforeEach
    public void setUp() {
        Product product = new Product();
        product.setName("xaxaxaxa");
        product.setQuantity(222);
        product.setId(111L);

        Mockito.when(productRepository.findProductByName(product.getName()))
                .thenReturn(Optional.of(product));
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        Product product = new Product();

        product.setName("some name");
        product.setQuantity(222);
        product.setId(111L);
        entityManager.persist(product);
        entityManager.flush();

        // when
        Optional<Product> found = productRepository.findProductByName(product.getName());

        // then
        assertThat(found.get().getName())
                .isEqualTo(product.getName());
    }

    @Test
    public void whenValidName_thenEmployeeShouldBeFound() {
        String name = "xaxaxaxa";
        Optional<Product> found = productRepository.findProductByName(name);

        assertThat(found.get().getName())
                .isEqualTo(name);
    }

}
