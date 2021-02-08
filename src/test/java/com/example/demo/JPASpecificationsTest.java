package com.example.demo;

import com.example.demo.entities.Product;
import com.example.demo.quering.ProductSpecification;
import com.example.demo.quering.SearchCriteria;
import com.example.demo.repositories.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JPASpecificationsTest
{

    @Autowired
    private ProductRepository repository;

    private Product product1;
    private Product product2;
    private Product product3;

    @PersistenceContext
    EntityManager em;


    @AfterEach
    private void flushData()
    {
        em.flush();
    }

    @Before
    public void init()
    {
        product1 = new Product();
        product1.setName("Lenovo");
        product1.setQuantity(5);
        product1.setDescription("john@doe.com");
        product1.setCategory("some category");
        repository.save(product1);

        product2 = new Product();
        product2.setName("Lenovo");
        product2.setQuantity(5555);
        product2.setDescription("lenovo@doe.com");
        product2.setCategory("No category ibm");
        repository.save(product2);

        product3 = new Product();
        product3.setName("Ibm");
        product3.setQuantity(15225);
        product3.setDescription("ibm@doe.com");
        product3.setCategory("some categ");
        repository.save(product3);
    }

    @Test
    public void givenName_whenGettingListOfProducts_thenCorrect()
    {
        ProductSpecification spec =
                new ProductSpecification(new SearchCriteria("name", ":", "Lenovo"));

        List<Product> results = repository.findAll(spec);

        assertTrue(results.contains(product1));

        assertTrue(results.contains(product2));
    }

    @Test
    public void givenNameAndCategory_whenGettingListOfProducts_thenCorrect()
    {
        ProductSpecification spec1 =
                new ProductSpecification(new SearchCriteria("name", ":", "Lenovo"));
        ProductSpecification spec2 =
                new ProductSpecification(new SearchCriteria("category", ":", "some category"));

        List<Product> results = repository.findAll(Specification.where(spec1).and(spec2));

        assertTrue(results.contains(product1));

        assertFalse(results.contains(product2));
        assertFalse(results.contains(product3));
    }

    @Test
    public void givenQuantity_whenGettingListOfProducts_thenCorrect()
    {
        ProductSpecification spec1 =
                new ProductSpecification(new SearchCriteria("quantity", ">", "100"));

        List<Product> results = repository.findAll(Specification.where(spec1));

        assertTrue(results.contains(product3));
        assertTrue(results.contains(product2));

    }
}