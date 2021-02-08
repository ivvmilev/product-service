package com.example.demo.controllers;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.exceptions.ExistingProductException;
import com.example.demo.exceptions.NotEnoughQuantityException;
import com.example.demo.exceptions.ProductNotFoundException;
import com.example.demo.hateos.ProductModelAssembler;
import com.example.demo.services.ProductService;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController
{
    public final ProductService productService;

    private final ProductModelAssembler assembler;

    public ProductController(ProductService productService, ProductModelAssembler assembler)
    {
        this.productService = productService;
        this.assembler = assembler;
    }

    @GetMapping("/products")
    public Slice<ProductDto> getProducts(
            @RequestParam(defaultValue = "1") int pageNumber
            , @RequestParam(defaultValue = "asc") int pageSize
            , @RequestParam(defaultValue = "name") String orderBy
            , @RequestParam(defaultValue = "asc") String sortDirection
    )
    {
        return productService.findPaginated(pageNumber, pageSize, orderBy, sortDirection);
    }

    @GetMapping("/allproduct")
    public List<ProductDto> getAllProducts(
            @RequestParam(required = false, defaultValue = "") String field
            , @RequestParam(required = false, defaultValue = "") String operation
            , @RequestParam(required = false, defaultValue = "") Object value
    )
    {
        return productService.listAllQueried(field, operation, value);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories()
    {
        return productService.getCategories();
    }

    @GetMapping({"", "/"})
    CollectionModel<EntityModel<ProductDto>> all()
    {
        List<EntityModel<ProductDto>> products = productService.getProductDto().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(products, linkTo(methodOn(ProductController.class).productService.getProductDto()).withSelfRel());
    }


    @GetMapping("/product/{id}")
    public EntityModel<ProductDto> getOneProduct(@PathVariable Long id)
    {
        try
        {
            ProductDto product = productService.getProductByIdDto(id);
            return assembler.toModel(product);
        } catch (ProductNotFoundException e)
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> saveProduct(@RequestBody @NotNull ProductDto newProduct) throws ExistingProductException
    {
        EntityModel<ProductDto> entityModel = assembler.toModel(productService.createProduct(newProduct));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id, @RequestBody ProductDto updateProduct)
    {
        EntityModel<ProductDto> entityModel = assembler.toModel(productService.updateProduct(updateProduct, id));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PostMapping("/product/{id}/order/{orderQuantity}")
    public ResponseEntity<?> orderProduct(@PathVariable long id, @PathVariable int orderQuantity)
    {
        try
        {
            ProductDto product = productService.orderProduct(id, orderQuantity);

            EntityModel<ProductDto> entityModel = assembler.toModel(product);

            return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (ProductNotFoundException e)
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product not found");
        } catch (NotEnoughQuantityException e)
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product quantity exceed");
        }
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") long id)
    {
        try
        {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e)
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product not found");
        }
    }
}
