package com.ai.ecommerce.repository;

import com.ai.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Không cần viết gì thêm, JpaRepository đã cân hết các hàm CRUD cơ bản
}
