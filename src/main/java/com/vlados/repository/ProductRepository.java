package com.vlados.repository;

import com.vlados.entity.Material;
import com.vlados.entity.Product;
import com.vlados.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByOrderByName(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update products p set p.name = :name, " +
            "p.category = :category, " +
            "p.material = :material, " +
            "p.picPath = :picPath, " +
            "p.price = :price, " +
            "p.description = :description " +
            "where p.id = :id")
    void updateProductById(@Param(value = "id") Long id,
                           @Param(value = "name") String name,
                           @Param(value = "category") ProductCategory category,
                           @Param(value = "material") Material material,
                           @Param(value = "picPath") String picPath,
                           @Param(value = "price") BigDecimal price,
                           @Param(value = "description") String description);

    @Query("select p from products p  " +
            "where p.material = :material AND " +
            "p.category = :category AND p.price in (:from, :to)")
    Page<Product> findProductsByMaterialAndCategoryAndPriceBetween(Pageable pageable,
                                                                   @Param(value = "material") String material,
                                                                   @Param(value = "category") String category,
                                                                   @Param(value = "from") int from,
                                                                   @Param(value = "to") int to);
}
