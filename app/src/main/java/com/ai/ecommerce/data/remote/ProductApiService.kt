package com.ai.ecommerce.data.remote

import com.ai.ecommerce.domain.model.Product
import retrofit2.http.GET

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(): List<Product>
}