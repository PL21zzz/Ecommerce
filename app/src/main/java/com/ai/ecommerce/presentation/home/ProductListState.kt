package com.ai.ecommerce.presentation.home

import com.ai.ecommerce.domain.model.Product

sealed interface ProductListState {
    object Loading : ProductListState
    data class Success(val products: List<Product>) : ProductListState
    data class Error(val message: String) : ProductListState
}