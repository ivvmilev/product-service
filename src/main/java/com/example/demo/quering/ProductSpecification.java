package com.example.demo.quering;

import com.example.demo.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Nonnull;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ProductSpecification implements Specification<Product>
{
    private final SearchCriteria criteria;

    public ProductSpecification(SearchCriteria searchCriteria)
    {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate
            (@Nonnull Root<Product> root, @Nonnull CriteriaQuery<?> query, @Nonnull CriteriaBuilder builder) {
        Object value = criteria.getValue();
        if (value.toString().isEmpty())
        {
            return null;
        }
        if (criteria.getOperation().equalsIgnoreCase(">")) {

            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getField()), value.toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getField()), value.toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getField()).getJavaType() == String.class) {
                return builder.like(
                        root.get(criteria.getField()), "%" + value + "%");
            } else {
                return builder.equal(root.get(criteria.getField()), value);
            }
        }
        return null;
    }
}
