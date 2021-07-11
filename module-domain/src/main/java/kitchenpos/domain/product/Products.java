package kitchenpos.domain.product;

import kitchenpos.common.exception.EntityNotExistsException;

import java.util.ArrayList;
import java.util.List;

public class Products {
    private List<Product> products;

    public Products(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public int size() {
        return products.size();
    }

    public Product findById(Long id) {
        return products.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(EntityNotExistsException::new);
    }
}