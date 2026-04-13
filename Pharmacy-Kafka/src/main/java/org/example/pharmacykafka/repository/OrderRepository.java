package org.example.pharmacykafka.repository;

import org.example.pharmacykafka.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,String> {
}
