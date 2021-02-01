package com.vlados.dto;

import com.vlados.entity.ProductCategory;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String name;
    private ProductCategory category;
    private BigDecimal price;
    private int amount;

}
