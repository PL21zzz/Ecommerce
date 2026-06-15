package com.ai.ecommerce.data.repository

import com.ai.ecommerce.data.remote.CoffeeDataSource
import com.ai.ecommerce.data.remote.ProductApiService
import com.ai.ecommerce.domain.model.Product
import com.ai.ecommerce.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        val rawList = apiService.getProducts()
        return CoffeeDataSource.mapToCoffeeList(rawList)
    }
}