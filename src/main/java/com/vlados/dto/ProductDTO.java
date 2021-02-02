package com.vlados.dto;

import com.vlados.entity.Material;
import com.vlados.entity.ProductCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String name;
    private ProductCategory category;
    private Material material;
    private String picPath;
    private LocalDateTime date;
    private BigDecimal price;
    private String description;
    private int amount;

}
