package com.vlados.dto;

import com.vlados.entity.Material;
import com.vlados.entity.ProductCategory;
import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    @NotEmpty(message = "{productName.empty}")
    private String name;

    private ProductCategory category;
    private Material material;
    private String picPath;
    private LocalDateTime date;

    @DecimalMin(value = "0.01", message = "{price.min}")
    private BigDecimal price;

    private String description;

    @PositiveOrZero(message = "{amount.min}")
    private Integer amount;

}
