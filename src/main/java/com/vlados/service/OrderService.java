package com.vlados.service;

import com.vlados.entity.Order;
import com.vlados.entity.Product;
import com.vlados.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
