package com.ai.ecommerce.domain.repository

import com.ai.ecommerce.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}