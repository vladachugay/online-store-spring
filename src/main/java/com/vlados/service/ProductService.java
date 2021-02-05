package com.vlados.service;

import com.vlados.entity.Material;
import com.vlados.entity.Product;
import com.vlados.entity.ProductCategory;
import com.vlados.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findByOrderByName(pageable);
    }

    public Page<Product> getFilteredProducts(Pageable pageable, String material, String category,
                                             BigDecimal from, BigDecimal to, String sortCriteria) {
        Page<Product> products;
        if (material.equals("ALL") && category.equals("ALL")) {
            products = productRepository.findProductsByPriceBetween(pageable, from, to);
        } else if (material.equals("ALL")) {
            products = productRepository.findProductsByCategoryAndPriceBetween(pageable,
                    ProductCategory.valueOf(category), from, to);
        } else if (category.equals("ALL")) {
            products = productRepository.findProductsByMaterialAndPriceBetween(pageable,
                    Material.valueOf(material), from, to);
        } else products = productRepository.findProductsByMaterialAndCategoryAndPriceBetween(pageable, Material.valueOf(material),
                ProductCategory.valueOf(category), from, to);
        //TODO sort products
        return products;
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

//    private Page<Product> sortProducts (Page<Product> products, SortCriteria sortCriteria) {
//        List<Product> list;
//        switch (sortCriteria) {
//            case BY_NAME_ASC: list = products.stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
//            case BY_NAME_DESC: list = products.stream().sorted(Comparator.comparing(Product::getName).reversed()).collect(Collectors.toList());
//            case NEW_IN: list = products.stream().sorted(Comparator.comparing(Product::getDate)).collect(Collectors.toList());
//            case PRICE_HIGH_TO_LOW: list = products.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
//            case PRICE_LOW_TO_HIGH: list = products.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
//
//        }
//    }
}
