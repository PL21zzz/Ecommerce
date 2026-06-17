package com.ai.ecommerce.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.ecommerce.domain.model.Product
import com.ai.ecommerce.domain.model.Rating
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class JavaProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val image: String,
    val rating: Double,
    val category: JavaCategory? = null // Nhận diện object danh mục lồng từ Java gửi sang
)

@Serializable
data class JavaCategory(
    val id: Int,
    val name: String
)

class ProductListViewModel : ViewModel() {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val _state = mutableStateOf<ProductListState>(ProductListState.Loading)
    val state: State<ProductListState> = _state

    // Lưu danh sách danh mục để đổ lên thanh trượt giao diện
    private val _categories = mutableStateOf<List<JavaCategory>>(emptyList())
    val categories: State<List<JavaCategory>> = _categories

    // Lưu danh mục hiện tại đang được chọn (Mặc định null nghĩa là chọn "All")
    private val _selectedCategoryId = mutableStateOf<Int?>(null)
    val selectedCategoryId: State<Int?> = _selectedCategoryId

    init {
        fetchCategories()
        fetchProducts(null) // Ban đầu load hết toàn bộ sản phẩm
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val fetchedCats = withContext(Dispatchers.IO) {
                    client.get("http://172.24.96.1:8080/api/categories").body<List<JavaCategory>>()
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
                // Xây dựng URL động: Nếu có categoryId thì bồi thêm param, không thì lấy hết
                val url = if (categoryId != null) {
                    "http://172.24.96.1:8080/api/products?categoryId=$categoryId"
                } else {
                    "http://172.24.96.1:8080/api/products"
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
                        category = javaProd.category?.name ?: "Coffee", // Lấy tên thật từ DB Java
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