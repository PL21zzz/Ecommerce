package com.ai.ecommerce.presentation.wishlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ai.ecommerce.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor() : ViewModel() {

    // Danh sách lưu trữ các sản phẩm được thả tim
    private val _wishlistItems = mutableStateListOf<Product>()
    val wishlistItems: List<Product> = _wishlistItems

    // Hàm đảo ngược trạng thái: Nếu chưa thích thì thêm vào, nếu thích rồi thì bỏ thích
    fun toggleWishlist(product: Product) {
        val exists = _wishlistItems.any { it.id == product.id }
        if (exists) {
            _wishlistItems.removeAll { it.id == product.id }
        } else {
            _wishlistItems.add(product)
        }
    }

    // Kiểm tra xem 1 sản phẩm cụ thể có đang được thích hay không để vẽ màu icon tim
    fun isFavorited(productId: Int): Boolean {
        return _wishlistItems.any { it.id == productId }
    }
}