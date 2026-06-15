package com.ai.ecommerce.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.ecommerce.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = mutableStateOf<ProductListState>(ProductListState.Loading)
    val state: State<ProductListState> = _state

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _state.value = ProductListState.Loading
            try {
                val coffeeProducts = repository.getProducts()
                _state.value = ProductListState.Success(coffeeProducts)
            } catch (e: Exception) {
                _state.value = ProductListState.Error(e.localizedMessage ?: "Lỗi kết nối mạng")
            }
        }
    }
}