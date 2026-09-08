package com.ai.ecommerce.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.ecommerce.data.remote.ApiConfig
import com.ai.ecommerce.domain.model.Product
import com.ai.ecommerce.domain.model.Rating
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class JavaProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val image: String,
    val rating: Double,
    val category: JavaCategory? = null
)

@Serializable
data class JavaCategory(
    val id: Int,
    val name: String
)

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf<ProductListState>(ProductListState.Loading)
    val state: State<ProductListState> = _state

    private val _categories = mutableStateOf<List<JavaCategory>>(emptyList())
    val categories: State<List<JavaCategory>> = _categories

    private val _selectedCategoryId = mutableStateOf<Int?>(null)
    val selectedCategoryId: State<Int?> = _selectedCategoryId

    init {
        fetchCategories()
        fetchProducts(null)
    }

    fun changeSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val fetchedCats = withContext(Dispatchers.IO) {
                    client.get("${ApiConfig.BASE_URL}categories").body<List<JavaCategory>>()
                }
                _categories.value = fetchedCats
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchProducts(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
        viewModelScope.launch {
            _state.value = ProductListState.Loading
            try {
                val url = if (categoryId != null) {
                    "${ApiConfig.BASE_URL}products?categoryId=$categoryId"
                } else {
                    "${ApiConfig.BASE_URL}products"
                }

                val javaProducts = withContext(Dispatchers.IO) {
                    client.get(url).body<List<JavaProduct>>()
                }

                val realProducts = javaProducts.map { javaProd ->
                    Product(
                        id = javaProd.id,
                        title = javaProd.title,
                        price = javaProd.price,
                        description = javaProd.description,
                        category = javaProd.category?.name ?: "Coffee",
                        image = javaProd.image,
                        rating = Rating(rate = javaProd.rating, count = 120)
                    )
                }

                _state.value = ProductListState.Success(products = realProducts)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = ProductListState.Error(message = "Lỗi kết nối: ${e.localizedMessage}")
            }
        }
    }
}
