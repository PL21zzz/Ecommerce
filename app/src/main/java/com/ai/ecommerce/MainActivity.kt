package com.ai.ecommerce

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.ai.ecommerce.domain.repository.ProductRepository
import com.ai.ecommerce.ui.theme.EcommerceTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var productRepository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. Chạy một Coroutine (bất đồng bộ) để gọi API mà không làm đơ máy
        lifecycleScope.launch {
            try {
                Log.d("TEST_DATA", "🚀 Đang kết nối lên FakeStoreAPI để lấy sản phẩm...")

                // Gọi hàm lấy dữ liệu thật
                val products = productRepository.getProducts()

                Log.d("TEST_DATA", "✅ THÀNH CÔNG RỰC RỠ! Lấy được ${products.size} sản phẩm.")

                // In thử tên và giá của sản phẩm đầu tiên ra xem đúng không
                if (products.isNotEmpty()) {
                    val firstProduct = products[0]
                    // Sửa chỗ này từ firstProduct.imageUrl thành firstProduct.image
                    // Sửa chỗ này từ firstProduct.rating thành firstProduct.rating.rate
                    Log.d("TEST_DATA", "📦 Sản phẩm đầu tiên: ${firstProduct.title} - Giá: ${firstProduct.price}$ - Điểm: ${firstProduct.rating.rate}")
                }
            } catch (e: Exception) {
                // Nếu mất mạng hoặc lỗi URL thì nó sẽ nhảy vào đây
                Log.e("TEST_DATA", "❌ LỖI RỒI BẠN ƠI: ${e.localizedMessage}")
            }
        }

        setContent {
            EcommerceTheme {
                // Tạm thời để trống giao diện để nhìn Logcat cho rõ
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    EcommerceTheme {
//        Greeting("Android")
//    }
//}