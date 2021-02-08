package com.example.demo.repositories;

import com.example.demo.dto.CategoryDto;
import com.example.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>
{
    Optional<Product> findProductByName(String name);

    @Query("Select p.category AS categoryName, sum(p.quantity) AS productsAvailable from Product p group by p.category, p.quantity order by p.category ASC")
    List<CategoryDto> getCategories();
}
