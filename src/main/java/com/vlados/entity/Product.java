package com.vlados.entity;

import com.vlados.dto.ProductDTO;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity(name = "products")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    @Enumerated(EnumType.STRING)
    private Material material;
    private String picPath;
    private LocalDateTime date;
    private String description;
    @Column(name = "price", precision = 8, scale = 2)
    private BigDecimal price;
    private Integer amount;
    @ManyToMany(mappedBy = "products")
    private Set<Order> orders;

    public Product(ProductDTO productDTO) {
        name = productDTO.getName();
        category = productDTO.getCategory();
        price = productDTO.getPrice();
        amount = productDTO.getAmount();
        picPath = productDTO.getPicPath();
        material = productDTO.getMaterial();
        description = productDTO.getDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) &&
                name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}


