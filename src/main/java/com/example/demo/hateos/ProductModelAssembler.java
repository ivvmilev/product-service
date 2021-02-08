package com.example.demo.hateos;

import com.example.demo.controllers.ProductController;
import com.example.demo.dto.ProductDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<ProductDto, EntityModel<ProductDto>>
{
    @Override
    @Nonnull
    public EntityModel<ProductDto> toModel(@Nonnull ProductDto product)
    {
        return EntityModel.of(product,
                linkTo(methodOn(ProductController.class)
                        .getOneProduct(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class)
                        .getProductDto()).withRel("products"));
    }
}
