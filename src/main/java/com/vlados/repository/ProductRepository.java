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
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByOrderByName(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update products p set p.name = :name, " +
            "p.category = :category, " +
            "p.material = :material, " +
            "p.picPath = :picPath, " +
            "p.price = :price, " +
            "p.description = :description," +
            "p.amount = :amount " +
            "where p.id = :id ")
    void updateProductById(@Param(value = "id") Long id,
                                   @Param(value = "name") String name,
                                   @Param(value = "category") ProductCategory category,
                                   @Param(value = "material") Material material,
                                   @Param(value = "picPath") String picPath,
                                   @Param(value = "price") BigDecimal price,
                                   @Param(value = "description") String description,
                                   @Param(value = "amount") Integer amount);

    List<Product> findProductsByMaterialAndCategoryAndPriceBetween(@Param(value = "material") Material material,
                                                                   @Param(value = "category") ProductCategory category,
                                                                   @Param(value = "from") BigDecimal from,
                                                                   @Param(value = "to") BigDecimal to);

    List<Product> findProductsByCategoryAndPriceBetween(@Param(value = "category") ProductCategory category,
                                                        @Param(value = "from") BigDecimal from,
                                                        @Param(value = "to") BigDecimal to);

    List<Product> findProductsByMaterialAndPriceBetween(@Param(value = "material") Material material,
                                                        @Param(value = "from") BigDecimal from,
                                                        @Param(value = "to") BigDecimal to);

    List<Product> findProductsByPriceBetween(@Param(value = "from") BigDecimal from,
                                           @Param(value = "to") BigDecimal to);

    @Transactional
    @Modifying
    @Query("update products p set p.amount = p.amount - 1 where p.id = :id")
    void incrementAmountById(@Param(value = "id") Long id);

}
