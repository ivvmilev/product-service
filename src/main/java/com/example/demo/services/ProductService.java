package com.example.demo.services;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductOrder;
import com.example.demo.entities.Product;
import com.example.demo.exceptions.Exceptions;
import com.example.demo.exceptions.ExistingProductException;
import com.example.demo.exceptions.NotEnoughQuantityException;
import com.example.demo.exceptions.ProductNotFoundException;
import com.example.demo.quering.ProductSpecification;
import com.example.demo.quering.SearchCriteria;
import com.example.demo.repositories.ProductRepository;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService
{
    @Autowired
    private final ProductRepository productRepository;

    private final RestTemplate restTemplate;

    public ProductService(ProductRepository productRepository, RestTemplate restTemplate)
    {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }


    public ProductDto createProduct(@NotNull ProductDto productDto) throws ExistingProductException
    {
        checkIfProductExists(productDto.getName());

        Product createdProduct = productRepository.save(dtoToProduct(productDto));

        ProductDto createdDto = new ProductDto();

        copyProps(createdProduct, createdDto);

        return createdDto;
    }

    private Product dtoToProduct(ProductDto productDto)
    {
        Product product = new Product();
        copyProps(productDto, product);
        return product;
    }

    public ProductDto orderProduct(long id, int quantity) throws ProductNotFoundException, NotEnoughQuantityException
    {
        Product product = getProductById(id);
        if (product.getQuantity() - quantity < 0)
        {
            throw new NotEnoughQuantityException(Exceptions.NOT_ENOUGH_QUANTITY);
        }

        boolean isSuccess = createOrder(id, quantity, product.getName());

        if (isSuccess)
        {
            product.setQuantity(product.getQuantity() - quantity);
            ProductDto productDto = new ProductDto();
            copyProps(productRepository.save(product), productDto);
            return productDto;
        }

        return null;
    }

    private boolean createOrder(long id, int quantity, String productName)
    {
        final String baseUrl = "http://order-service/order";
        URI uri;
        try
        {
            uri = new URI(baseUrl);
            ProductOrder productOrder = new ProductOrder(id, quantity, productName);

            ResponseEntity<String> result = restTemplate.postForEntity(uri, productOrder, String.class);
            if (result.getStatusCode() != HttpStatus.OK)
            {
                return false;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "Order-service is unavailable, please try again later.");
        }

        return true;
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



    public List<CategoryDto> getCategories()
    {
        return productRepository.getCategories();
    }

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    public List<ProductDto> getAllFiltered(ProductSpecification productSpecification)
    {
        return getProductDto(productRepository.findAll(productSpecification));
    }

    public Product getProductById(Long id) throws ProductNotFoundException
    {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductDto getProductByIdDto(Long id) throws ProductNotFoundException
    {
        ProductDto productDto = new ProductDto();
        copyProps(getProductById(id), productDto);
        return productDto;
    }

    public Page<ProductDto> findPaginated(int pageNo, int pageSize, String orderBy, String sortDirection)
    {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending() :
                Sort.by(orderBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return new PageImpl<>(getProductDto(this.productRepository.findAll(pageable).getContent()));
    }

    public List<ProductDto> getProductDto(List<Product> products)
    {
        List<Product> productsList = products;

        if (productsList == null)
        {
            productsList = getAllProducts();
        }

        return productsList.stream()
                .map(product ->
                {
                    ProductDto productDto = new ProductDto();
                    try
                    {
                        copyProps(product, productDto);
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    return productDto;
                })
                .collect(Collectors.toList());
    }

    public List<ProductDto> listAllQueried(String field, String operation, Object value)
    {
        ProductSpecification spec =
                new ProductSpecification(new SearchCriteria(field, operation, value));
        return getAllFiltered(spec);
    }

    public ProductDto updateProduct(ProductDto update, long id)
    {
        if (productRepository.findById(id).isPresent())
        {
            Product existingProduct = productRepository.findById(id).get();

            copyProps(update, existingProduct);

            Product updatedProduct = productRepository.save(existingProduct);

            ProductDto productDto = new ProductDto();

            copyProps(updatedProduct, productDto);

            return productDto;
        } else
        {
            return null;
        }
    }

    public void copyProps(Object oldObj, Object newObj)
    {
        BeanUtils.copyProperties(oldObj, newObj);
    }
}
