package com.ai.ecommerce.presentation.cart

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ai.ecommerce.data.remote.ApiConfig
import com.ai.ecommerce.domain.model.Product
import com.ai.ecommerce.presentation.auth.AuthUser
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import javax.inject.Inject

data class CartItem(
    val product: Product,
    val size: String,
    val quantity: Int
)

enum class PaymentMethod {
    VIET_QR,
    COD,
    VNPAY
}

@Serializable
data class CheckoutItemRequest(
    val productId: Int,
    val productTitle: String,
    val image: String,
    val size: String,
    val unitPrice: Double,
    val quantity: Int
)

@Serializable
data class CreatePaymentRequest(
    val userId: Long,
    val totalAmount: Double,
    val deliveryFee: Double,
    val customerName: String,
    val phone: String,
    val address: String,
    val note: String,
    val items: List<CheckoutItemRequest>
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> = _cartItems

    val subtotal: Double get() = _cartItems.sumOf { it.product.price * it.quantity }
    val deliveryFee: Double = 1.0
    val totalItemsCount: Int get() = _cartItems.sumOf { it.quantity }
    val total: Double get() = subtotal + deliveryFee

    fun addToCart(product: Product, size: String = "M") {
        val existingItem = _cartItems.find { it.product.id == product.id && it.size == size }
        if (existingItem != null) {
            val index = _cartItems.indexOf(existingItem)
            _cartItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            _cartItems.add(CartItem(product = product, size = size, quantity = 1))
        }
    }

    fun updateQuantity(productId: Int, size: String, delta: Int) {
        val item = _cartItems.find { it.product.id == productId && it.size == size } ?: return
        val newQuantity = item.quantity + delta
        if (newQuantity > 0) {
            val index = _cartItems.indexOf(item)
            _cartItems[index] = item.copy(quantity = newQuantity)
        } else {
            _cartItems.remove(item)
        }
    }

    suspend fun createCodOrder(
        currentUser: AuthUser,
        customerName: String,
        phone: String,
        address: String,
        note: String
    ): Result<String> {
        return try {
            val request = buildPaymentRequest(currentUser, customerName, phone, address, note)
            val response = client.post("${ApiConfig.BASE_URL}vnpay/create-cod-order") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val map = response.body<Map<String, String>>()
                Result.success(map["message"] ?: "Order placed successfully")
            } else {
                Result.failure(Exception("Server status ${response.status.value}: Please restart Spring Boot backend."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createVietQrOrder(
        currentUser: AuthUser,
        customerName: String,
        phone: String,
        address: String,
        note: String
    ): Result<Map<String, String>> {
        return try {
            val request = buildPaymentRequest(currentUser, customerName, phone, address, note)
            val response = client.post("${ApiConfig.BASE_URL}vnpay/create-vietqr-order") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val map = response.body<Map<String, String>>()
                Result.success(map)
            } else {
                Result.failure(Exception("Server status ${response.status.value}: Please restart Spring Boot backend."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPaymentUrl(
        currentUser: AuthUser,
        customerName: String,
        phone: String,
        address: String,
        note: String
    ): Result<String> {
        return try {
            val request = buildPaymentRequest(currentUser, customerName, phone, address, note)
            val response = client.post("${ApiConfig.BASE_URL}vnpay/create-payment") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val map = response.body<Map<String, String>>()
                val url = map["url"].orEmpty()
                if (url.isNotEmpty()) Result.success(url) else Result.failure(Exception("Could not create payment link."))
            } else {
                Result.failure(Exception("Server status ${response.status.value}: Please restart Spring Boot backend."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildPaymentRequest(
        currentUser: AuthUser,
        customerName: String,
        phone: String,
        address: String,
        note: String
    ): CreatePaymentRequest {
        return CreatePaymentRequest(
            userId = currentUser.id,
            totalAmount = total,
            deliveryFee = deliveryFee,
            customerName = customerName,
            phone = phone,
            address = address,
            note = note,
            items = _cartItems.map { item ->
                CheckoutItemRequest(
                    productId = item.product.id,
                    productTitle = item.product.title,
                    image = item.product.image,
                    size = item.size,
                    unitPrice = item.product.price,
                    quantity = item.quantity
                )
            }
        )
    }

    fun clearCart() {
        _cartItems.clear()
    }
}
