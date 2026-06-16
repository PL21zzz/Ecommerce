package com.ai.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.ecommerce.model.Product;
import com.ai.ecommerce.repository.ProductRepository;

@RestController
@RequestMapping("/api/products") // Đường dẫn API mà Android đang gọi
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // API hứng lệnh GET từ Android để lấy 10 ly Coffee trong Postgres đổ về
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
