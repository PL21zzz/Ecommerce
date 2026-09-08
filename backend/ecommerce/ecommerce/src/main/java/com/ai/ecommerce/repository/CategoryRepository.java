package com.ai.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.ecommerce.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
