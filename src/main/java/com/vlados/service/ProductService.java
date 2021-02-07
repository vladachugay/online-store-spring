package com.vlados.service;

import com.vlados.entity.Material;
import com.vlados.entity.Product;
import com.vlados.entity.ProductCategory;
import com.vlados.entity.SortCriteria;
import com.vlados.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public Page<Product> getFilteredProducts(Pageable pageable, String material,
                                             String category, BigDecimal from,
                                             BigDecimal to, String sortCriteria) {
        List<Product> products;
        if (material.equals("ALL") && category.equals("ALL")) {
            products = productRepository.findProductsByPriceBetween(from, to);

        } else if (material.equals("ALL")) {
            products = productRepository.findProductsByCategoryAndPriceBetween(ProductCategory.valueOf(category), from, to);

        } else if (category.equals("ALL")) {
            products = productRepository.findProductsByMaterialAndPriceBetween(Material.valueOf(material), from, to);

        } else products = productRepository.findProductsByMaterialAndCategoryAndPriceBetween(Material.valueOf(material),
                ProductCategory.valueOf(category), from, to);

        int fromIndex = pageable.getPageNumber() * pageable.getPageSize();
        int toIndex = Math.min(pageable.getPageNumber() * pageable.getPageSize() + pageable.getPageSize(), products.size());

        sortProducts(products, SortCriteria.valueOf(sortCriteria));
        return new PageImpl<>(products.subList(fromIndex, toIndex), pageable, products.size());
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

    private void sortProducts (List<Product> products, SortCriteria sortCriteria) {
        switch (sortCriteria) {
            case BY_NAME_ASC: products.sort(Comparator.comparing(Product::getName)); break;
            case BY_NAME_DESC: products.sort(Comparator.comparing(Product::getName).reversed()); break;
            case NEW_IN: products.sort(Comparator.comparing(Product::getDate)); break;
            case PRICE_HIGH_TO_LOW: products.sort(Comparator.comparing(Product::getPrice).reversed()); break;
            case PRICE_LOW_TO_HIGH: products.sort(Comparator.comparing(Product::getPrice)); break;
        }
    }

    public void incrementAmount(Product product) {
        //TODO catch exception (product doesnt exist or not enough products)
        productRepository.incrementAmountById(product.getId());
    }
}
