package com.github.maximovj.rhhub_app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification<T> {

    protected Specification<T> spec = Specification.where(null);

    protected Specification<T> equalsSpec(String field, Object value) {
        return (root, query, cb) ->
                value == null ? null : cb.equal(root.get(field), value);
    }

    protected Specification<T> likeIgnoreCase(String field, String value) {
        return (root, query, cb) ->
                (value == null || value.isBlank())
                        ? null
                        : cb.like(
                            cb.lower(root.get(field)),
                            "%" + value.toLowerCase() + "%"
                        );
    }

    public Specification<T> build(){ 
        return spec;
    }
    
}
