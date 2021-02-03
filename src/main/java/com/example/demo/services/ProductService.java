package com.example.demo.services;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService
{
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public Product createProduct(@NotNull Product product) throws ExistingProductException
    {
        checkIfProductExists(product.getName());
        return productRepository.save(product);
    }

    public Product orderProduct(long id, int quantity) throws ProductNotFoundException, NotEnoughQuantityException
    {
        Product product = getProductById(id);
        if (product.getQuantity() - quantity < 0)
        {
            throw new NotEnoughQuantityException(Exceptions.NOT_ENOUGH_QUANTITY);
        }
        // TODO: 2.2.2021 г. call api to order the product -> if success continue
        product.setQuantity(product.getQuantity() - quantity);
        return productRepository.save(product);
    }

    private void checkIfProductExists(String name) throws ExistingProductException
    {
        Optional<Product> existingProduct = getProductByName(name);

        if (existingProduct.isPresent())
        {
            throw new ExistingProductException(Exceptions.EXISTING_USER_EXCEPTION);
        }
    }

    public Optional<Product> getProductByName(String name)
    {
        return productRepository.findProductByName(name);
    }

    public void deleteProduct(long id) throws ProductNotFoundException
    {
        Optional<Product> existingUser = productRepository.findById(id);

        if (existingUser.isPresent())
        {
            productRepository.deleteById(id);
        } else
        {
            throw new ProductNotFoundException(id);
        }
    }

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) throws ProductNotFoundException
    {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Page<Product> findPaginated(int pageNo, String sortField, String sortDirection)
    {
        final int pageSize = 10;
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.productRepository.findAll(pageable);
    }
}
