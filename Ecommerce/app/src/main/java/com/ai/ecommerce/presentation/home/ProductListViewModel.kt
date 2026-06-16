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

// 1. Tạo Class đệm nằm độc lập ở ngoài để hứng cấu trúc phẳng từ Java
@Serializable
data class JavaProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val image: String,
    val rating: Double // Hứng đúng con số thực 4.8 từ Java gửi sang
)

class ProductListViewModel : ViewModel() {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Bỏ qua nếu lệch các trường khác
            })
        }
    }

    private val _state = mutableStateOf<ProductListState>(ProductListState.Loading)
    val state: State<ProductListState> = _state

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _state.value = ProductListState.Loading
            try {
                // 2. Ktor gọi API lấy danh sách phẳng dạng JavaProduct về
                val javaProducts = withContext(Dispatchers.IO) {
                    client.get("http://172.24.96.1:8080/api/products").body<List<JavaProduct>>()
                }

                // 3. Tự chuyển đổi cấu trúc phẳng thành Object lồng nhau mà UI Android cũ đang cần
                val realProducts = javaProducts.map { javaProd ->
                    Product(
                        id = javaProd.id,
                        title = javaProd.title,
                        price = javaProd.price,
                        description = javaProd.description,
                        category = "Coffee", // Tự bù trường category
                        image = javaProd.image,
                        // Tự đóng gói con số 4.8 thành Object Rating(rate=4.8, count=120) cho UI cũ đọc mượt mà
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