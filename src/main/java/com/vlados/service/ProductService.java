package com.vlados.service;

import com.vlados.dto.ProductDTO;
import com.vlados.entity.Product;
import com.vlados.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void saveProduct(Product product) {
            productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.updateProductById(product.getId(), product.getName(),
                product.getCategory(),
                product.getMaterial(),
                product.getPicPath(),
                product.getPrice(),
                product.getDescription());
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }
}
