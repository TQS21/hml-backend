package com.service.hml.repositories;

import com.service.hml.entities.OrderStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<OrderStats, Long> {
    List<OrderStats> findByOrderId(int id);
}
