package com.vlados.dto;

import com.vlados.entity.OrderStatus;
import com.vlados.entity.Product;
import com.vlados.entity.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private BigDecimal totalPrice;
    private LocalDateTime creationDate;
    private OrderStatus status;
    private User user;

    private Set<Product> products;
}
