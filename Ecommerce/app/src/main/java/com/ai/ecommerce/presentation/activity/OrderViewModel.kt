package com.ai.ecommerce.presentation.activity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.ecommerce.data.remote.ApiConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class OrderHistoryItem(
    val id: Int,
    val orderCode: String,
    val totalAmount: Double,
    val status: String,
    val createdAt: String = "",
    val customerName: String? = null,
    val address: String? = null,
    val items: List<OrderHistoryLineItem> = emptyList()
)

@Serializable
data class OrderHistoryLineItem(
    val productTitle: String? = null,
    val size: String? = null,
    val quantity: Int = 0
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {

    private val _orders = mutableStateOf<List<OrderHistoryItem>>(emptyList())
    val orders: State<List<OrderHistoryItem>> = _orders

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun loadOrders(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _orders.value = client.get("${ApiConfig.BASE_URL}vnpay/orders?userId=$userId").body()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Could not load orders."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
