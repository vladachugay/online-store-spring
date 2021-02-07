package com.vlados.entity;

import com.vlados.dto.ProductDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Min(value = 0)
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
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


