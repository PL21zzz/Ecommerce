package com.ai.ecommerce.presentation.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ai.ecommerce.ui.theme.SurfaceLight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ai.ecommerce.ui.theme.BackgroundLight
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.TextPrimary

@Composable
fun MyOrdersScreen(
    userId: Long,
    onBackClick: () -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val orders = viewModel.orders.value
    val errorMessage = viewModel.errorMessage.value

    LaunchedEffect(userId) {
        viewModel.loadOrders(userId)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(BackgroundLight).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBackIosNew, "Back", Modifier.clickable { onBackClick() }, tint = TextPrimary)
                Spacer(modifier = Modifier.width(16.dp))
                Text("My Orders", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            errorMessage?.let { message ->
                item {
                    Text(message, color = Color(0xFFED5151), fontSize = 14.sp)
                }
            }

            if (orders.isEmpty() && errorMessage == null) {
                item {
                    Text("No paid or failed orders yet.", color = TextPrimary, fontSize = 14.sp)
                }
            }

            items(orders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = order.orderCode, fontWeight = FontWeight.Bold, color = TextPrimary)
                            if (order.createdAt.isNotBlank()) {
                                Text(text = "Date: ${order.createdAt.asDisplayDateTime()}", color = TextPrimary, fontSize = 12.sp)
                            }
                            Text(text = "${order.items.sumOf { it.quantity }} items", color = TextPrimary, fontSize = 12.sp)
                            Text(text = "Total: $${String.format("%.2f", order.totalAmount)}", color = CoffeeOrange)
                        }

                        Box(
                            modifier = Modifier
                                .background(order.statusColor(), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = order.status, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

private fun OrderHistoryItem.statusColor(): Color {
    return when (status) {
        "PAID" -> Color(0xFF4CAF50)
        "PENDING" -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }
}

private fun String.asDisplayDateTime(): String {
    return take(16).replace("T", " ")
}
