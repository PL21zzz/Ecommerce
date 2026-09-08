package com.ai.ecommerce.data.remote

import retrofit2.http.GET

data class BackendCategoryDto(
    val id: Int,
    val name: String
)

data class BackendProductDto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val image: String,
    val rating: Double,
    val category: BackendCategoryDto? = null
)

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(): List<BackendProductDto>
}
