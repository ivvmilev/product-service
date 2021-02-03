package com.example.demo;

import com.example.demo.controllers.ProductController;
import com.example.demo.entities.Product;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>>
{
    @SneakyThrows
    @Override
    public EntityModel<Product> toModel(Product product)
    {
        return EntityModel.of(product,
                linkTo(methodOn(ProductController.class).getOneProduct(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));
    }
}
