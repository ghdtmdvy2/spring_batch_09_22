package com.ll.exam.app_2022_09_22.app.order.repository;

import com.ll.exam.app_2022_09_22.app.order.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findAllByIdBetween(long startId,long endId, Pageable pageable);
}