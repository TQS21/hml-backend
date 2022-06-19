package com.service.hml.repositories;

import com.service.hml.entities.OrderStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<OrderStats, Long> {
}
