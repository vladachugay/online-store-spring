package com.vlados.service;

import com.vlados.dto.OrderDTO;
import com.vlados.entity.*;
import com.vlados.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;

    public Page<Order> getOrders(Pageable pageable) {
        try {
            return orderRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("{} while getting orders", e.getMessage());
            throw new RuntimeException();
        }
    }

    public List<Order> getOrdersByUser(User user) {
        try {
            return orderRepository.findAllByUser(user);
        } catch (Exception e) {
            log.error("{} while getting orders for user {}", e.getMessage(), user.getUsername());
            throw new RuntimeException();
        }
    }

    @Transactional
    public void createOrder(Cart cart) {
        productService.decrementAmountForProducts(new ArrayList<>(cart.getCartProducts()));
        try {
            orderRepository.save(new Order(OrderDTO.builder()
                    .totalPrice(cart.getTotalPrice())
                    .creationDate(LocalDateTime.now())
                    .status(OrderStatus.REGISTERED)
                    .user(userService.getCurrentUser())
                    .products(cart.getCartProducts()).build()));
        } catch (Exception e) {
            log.error("{} while creating new order", e.getMessage());
            throw new RuntimeException();
        }
    }

    public void setStatusPaid(Order order) {
        try {
            orderRepository.setStatusPaid(order.getId());
        } catch (Exception e) {
            log.error("{} while changing order status to paid (order id - {})", e.getMessage(), order.getId());
            throw new RuntimeException();
        }
    }

    @Transactional
    public void cancelOrder(Order order) {
        productService.incrementAmountForProducts(new ArrayList<>(order.getProducts()));

        try {
            orderRepository.setStatusCanceled(order.getId());
        } catch (Exception e) {
            log.error("{} while changing order status canceled (order id - {})", e.getMessage(), order.getId());
            throw new RuntimeException();
        }
    }
}
