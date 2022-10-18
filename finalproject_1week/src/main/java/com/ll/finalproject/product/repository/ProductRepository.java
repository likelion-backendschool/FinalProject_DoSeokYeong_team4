package com.ll.finalproject.product.repository;

import com.ll.finalproject.product.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
