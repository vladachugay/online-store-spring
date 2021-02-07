package com.vlados.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
@SessionScope
@Getter
@Setter
public class Cart {
    Set<Product> cartProducts;
    BigDecimal totalPrice;

    public Cart() {
        cartProducts = new HashSet<>();
        totalPrice = new BigDecimal(0);
    }

    public void addProduct(Product product) {
        if (cartProducts.add(product)) totalPrice = totalPrice.add(product.getPrice());
    }

    public void deleteProduct(Product product) {
        if (cartProducts.remove(product)) totalPrice = totalPrice.subtract(product.getPrice());
    }

    public void clear() {
        cartProducts.clear();
        totalPrice = BigDecimal.valueOf(0);
    }
}
