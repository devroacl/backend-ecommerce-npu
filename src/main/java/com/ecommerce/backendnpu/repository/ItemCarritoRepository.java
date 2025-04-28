package com.ecommerce.backendnpu.repository;

import com.ecommerce.backendnpu.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCarritoRepository extends JpaRepository<CarritoItem, Long> {
}