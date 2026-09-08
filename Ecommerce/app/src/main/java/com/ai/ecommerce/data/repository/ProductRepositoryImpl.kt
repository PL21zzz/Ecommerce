package com.ai.ecommerce.data.repository

import com.ai.ecommerce.data.remote.ProductApiService
import com.ai.ecommerce.domain.model.Product
import com.ai.ecommerce.domain.model.Rating
import com.ai.ecommerce.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        return apiService.getProducts().map { product ->
            Product(
                id = product.id,
                title = product.title,
                price = product.price,
                description = product.description,
                category = product.category?.name ?: "Coffee",
                image = product.image,
                rating = Rating(rate = product.rating, count = 120)
            )
        }
    }
}
