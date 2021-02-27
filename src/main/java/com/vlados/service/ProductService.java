package com.vlados.service;

import com.vlados.dto.ProductDTO;
import com.vlados.entity.Material;
import com.vlados.entity.Product;
import com.vlados.entity.ProductCategory;
import com.vlados.entity.SortCriteria;
import com.vlados.exception.store_exc.CantDeleteBecauseOfOrderException;
import com.vlados.exception.store_exc.DuplicateProductNameException;
import com.vlados.exception.store_exc.NotEnoughProductsException;
import com.vlados.repository.ProductRepository;
import com.vlados.util.ExceptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
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

    public void saveProduct(ProductDTO productDTO) {
        productDTO.setDate(LocalDateTime.now());
        try {
            productRepository.save(new Product(productDTO));
        } catch (Exception e) {
            log.error("{} while saving new product", e.getMessage());
            throw new DuplicateProductNameException(ExceptionKeys.DUPLICATE_PRODUCT_NAME);
        }
    }

    public void updateProduct(Long productId, Product product) {
        try {
            productRepository.updateProductById(productId, product.getName(),
                    product.getCategory(),
                    product.getMaterial(),
                    product.getPrice(),
                    product.getDescription(),
                    product.getAmount());
        } catch (Exception e) {
            log.error("{} while updating product with id {}", e.getMessage(), productId);
            throw new DuplicateProductNameException(ExceptionKeys.DUPLICATE_PRODUCT_NAME);
        }
    }

    public void deleteProduct(Product product) {
        try {
            productRepository.delete(product);
        } catch (RuntimeException e) {
            log.error("{} while trying to delete product with id {}", e.getMessage(), product.getId());
            throw new CantDeleteBecauseOfOrderException(ExceptionKeys.CANT_DELETE_BECAUSE_OF_ORDER);
        }
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

    public void incrementAmountForProducts(List<Product> products) {
        try {
            productRepository.incrementAmountById(
                    products.stream()
                            .map(Product::getId)
                            .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("{} while trying to increment product amount", e.getMessage());
            throw new NotEnoughProductsException(ExceptionKeys.NOT_ENOUGH_PRODUCTS);
        }
    }

    public void decrementAmountForProducts(List<Product> products) {
        try {
            productRepository.decrementAmountById(
                    products.stream()
                            .map(Product::getId)
                            .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("{} while trying to decrement product amount", e.getMessage());
            throw new RuntimeException();
        }
    }
}
