package com.ai.ecommerce.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.presentation.cart.components.CartItemRow
import com.ai.ecommerce.presentation.cart.components.EmptyCart
import com.ai.ecommerce.presentation.cart.components.OrderSummarySection
import com.ai.ecommerce.ui.theme.*

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBackClick: () -> Unit,
    onGoShoppingClick: () -> Unit // Thêm callback để nhấn quay lại trang chủ mua hàng khi giỏ trống
) {
    val isCartEmpty = viewModel.cartItems.isEmpty()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBackIosNew, "Back", Modifier.clickable { onBackClick() }, tint = TextPrimary)
                Text("Cart", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                // Chỉ hiện nút xóa sạch giỏ hàng khi giỏ có đồ
                if (!isCartEmpty) {
                    Icon(Icons.Default.DeleteOutline, "Clear", Modifier.clickable { viewModel.clearCart() }, tint = TextPrimary)
                } else {
                    Spacer(modifier = Modifier.size(24.dp))
                }
            }
        },
        bottomBar = {
            // Chỉ hiển thị nút Place Order khi giỏ hàng có sản phẩm
            if (!isCartEmpty) {
                Box(modifier = Modifier.background(BackgroundLight).padding(24.dp)) {
                    Button(
                        onClick = { /* Xử lý đặt hàng */ },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CoffeeOrange),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Place Order", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        },
        containerColor = BackgroundLight
    ) { padding ->
        if (isCartEmpty) {
            // Gọi màn hình trống nếu không có sản phẩm
            EmptyCart(
                onGoShoppingClick = onGoShoppingClick,
                modifier = Modifier.padding(padding)
            )
        } else {
            // Gọi danh sách và tổng kết hóa đơn đã được bóc tách
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.cartItems) { item ->
                    CartItemRow(
                        item = item,
                        onIncrease = { viewModel.updateQuantity(item.product.id, 1) },
                        onDecrease = { viewModel.updateQuantity(item.product.id, -1) }
                    )
                }

                item {
                    OrderSummarySection(
                        subtotal = viewModel.subtotal,
                        deliveryFee = viewModel.deliveryFee,
                        total = viewModel.total
                    )
                }
            }
        }
    }
}