package com.example.demo.controllers;

import com.example.demo.ProductModelAssembler;
import com.example.demo.entities.Product;
import com.example.demo.services.ExistingProductException;
import com.example.demo.services.NotEnoughQuantityException;
import com.example.demo.services.ProductNotFoundException;
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
    private final ProductService productService;

    private final ProductModelAssembler assembler;

    public ProductController(ProductService productService, ProductModelAssembler assembler)
    {
        this.productService = productService;
        this.assembler = assembler;
    }

    @GetMapping("/product")
    public Slice<Product> getProducts(
            @RequestParam(defaultValue = "1") int pageNumber
            , @RequestParam(defaultValue = "name") String sortField
            , @RequestParam(defaultValue = "asc") String order
    )
    {
        return productService.findPaginated(pageNumber, sortField, order);
    }


    @GetMapping("/login")
    public String loginPage()
    {
        return "login";
    }

    @GetMapping({"", "/"})
    CollectionModel<EntityModel<Product>> all()
    {
        List<EntityModel<Product>> products = getAllProducts().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(products, linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
    }

    public List<Product> getAllProducts()
    {
        return productService.getAllProducts();
    }

    @GetMapping("/product/{id}")
    public EntityModel<Product> getOneProduct(@PathVariable Long id) throws ProductNotFoundException
    {
        Product user = productService.getProductById(id);

        return assembler.toModel(user);
    }

    @PostMapping("/product")
    public ResponseEntity<?> saveProduct(@RequestBody @NotNull Product newProduct) throws ExistingProductException
    {
        EntityModel<Product> entityModel = assembler.toModel(productService.createProduct(newProduct));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PostMapping("/product/{id}/order/{orderQuantity}")
    public ResponseEntity<?> orderProduct(@PathVariable long id, @PathVariable int orderQuantity)
    {
        try
        {
            productService.orderProduct(id, orderQuantity);
            EntityModel<Product> entityModel = assembler.toModel(productService.orderProduct(id, orderQuantity));

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
