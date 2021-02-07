package com.vlados.service;

import com.vlados.dto.OrderDTO;
import com.vlados.entity.*;
import com.vlados.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;

    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findAllByUser(user);
    }

    @Transactional
    public void createOrder(Cart cart) {
        cart.getCartProducts().forEach(productService::incrementAmount);

        saveNewOrder(OrderDTO.builder()
                .totalPrice(cart.getTotalPrice())
                .creationDate(LocalDateTime.now())
                .status(OrderStatus.REGISTERED)
                .user(userService.getCurrentUser())
                .products(cart.getCartProducts()).build());
    }

    private void saveNewOrder(OrderDTO orderDTO) {
        orderRepository.save(new Order(orderDTO));
    }

    public void changeStatus(OrderStatus status, Order order) {
        orderRepository.changeStatus(status, order.getId());
    }
}
