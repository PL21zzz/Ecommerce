package com.ai.ecommerce.presentation.cart

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ai.ecommerce.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Model nhỏ để quản lý Sản phẩm + Số lượng trong giỏ
data class CartItem(
    val product: Product,
    var quantity: Int
)

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    // Danh sách các món trong giỏ hàng (Dùng StateList để Compose tự cập nhật UI)
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> = _cartItems

    // Tính toán tiền bạc dựa trên dữ liệu trong giỏ
    val subtotal: Double get() = _cartItems.sumOf { it.product.price * it.quantity }
    val deliveryFee: Double = 1.0 // Phí ship cố định 1$ theo ảnh mẫu
    val total: Double get() = subtotal + deliveryFee

    fun addToCart(product: Product) {
        val existingItem = _cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++
            // Force cập nhật list để UI nhận biết
            val index = _cartItems.indexOf(existingItem)
            _cartItems[index] = existingItem.copy()
        } else {
            _cartItems.add(CartItem(product, 1))
        }
    }

    fun updateQuantity(productId: Int, delta: Int) {
        val item = _cartItems.find { it.product.id == productId }
        if (item != null) {
            val newQuantity = item.quantity + delta
            if (newQuantity > 0) {
                val index = _cartItems.indexOf(item)
                _cartItems[index] = item.copy(quantity = newQuantity)
            } else {
                _cartItems.remove(item) // Về 0 thì xóa khỏi giỏ
            }
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }
}