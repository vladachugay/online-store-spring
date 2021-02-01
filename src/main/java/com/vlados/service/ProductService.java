package com.vlados.service;

import com.vlados.entity.Product;
import com.vlados.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findByOrderByName(pageable);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
