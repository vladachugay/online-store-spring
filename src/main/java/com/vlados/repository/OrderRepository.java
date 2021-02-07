package com.vlados.repository;

import com.vlados.entity.Order;
import com.vlados.entity.OrderStatus;
import com.vlados.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAll(Pageable pageable);

    List<Order> findAllByUser(User user);

    @Transactional
    @Modifying
    @Query("update orders o set o.status = :status " +
            "where o.id = :id")
    void changeStatus(OrderStatus status, Long id);
}
