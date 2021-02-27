package com.vlados.entity;

import com.vlados.dto.ProductDTO;
import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
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
@Check(constraints = "amount >= 0")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "name")
    @NotEmpty(message = "{productName.empty}")
    private String name;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    private Material material;

    private LocalDateTime date;
    private String description;

    @Column(name = "price", precision = 8, scale = 2)
    @DecimalMin(value = "0.01", message = "{price.min}")
    private BigDecimal price;

    @Column(name = "amount")
    @PositiveOrZero(message = "{amount.min}")
    private Integer amount;

    @ManyToMany(mappedBy = "products")
    private Set<Order> orders;

    public Product(ProductDTO productDTO) {
        name = productDTO.getName();
        category = productDTO.getCategory();
        price = productDTO.getPrice();
        amount = productDTO.getAmount();
        material = productDTO.getMaterial();
        description = productDTO.getDescription();
        date = productDTO.getDate();
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

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", material=" + material +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}


