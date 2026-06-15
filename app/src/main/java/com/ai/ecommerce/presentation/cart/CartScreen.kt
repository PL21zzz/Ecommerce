package com.ai.ecommerce.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ai.ecommerce.ui.theme.*

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBackClick: () -> Unit
) {
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
                Icon(Icons.Default.DeleteOutline, "Clear", Modifier.clickable { viewModel.clearCart() }, tint = TextPrimary)
            }
        },
        bottomBar = {
            // Nút Place Order to đùng màu Cam đất
            Box(modifier = Modifier.background(BackgroundLight).padding(24.dp)) {
                Button(
                    onClick = { /* Order logic */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeOrange),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Place Order", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        },
        containerColor = BackgroundLight
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Danh sách sản phẩm trong giỏ
            items(viewModel.cartItems) { item ->
                CartItemRow(item, onIncrease = { viewModel.updateQuantity(item.product.id, 1) }, onDecrease = { viewModel.updateQuantity(item.product.id, -1) })
            }

            // Phần Order Summary
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Order Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                SummaryRow("Subtotal", "$ ${String.format("%.2f", viewModel.subtotal)}")
                SummaryRow("Delivery Fee", "$ ${String.format("%.2f", viewModel.deliveryFee)}")
                Divider(Modifier.padding(vertical = 12.dp), color = BorderColor)
                SummaryRow("Total", "$ ${String.format("%.2f", viewModel.total)}", isTotal = true)
            }

            // Phần Payment Method
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceLight, RoundedCornerShape(16.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CreditCard, null, tint = CoffeeOrange)
                    Spacer(Modifier.width(12.dp))
                    Text("Card: ...4242", modifier = Modifier.weight(1f), color = TextPrimary, fontWeight = FontWeight.Medium)
                    Icon(Icons.Default.KeyboardArrowRight, null, tint = TextPrimary)
                }
                Spacer(Modifier.height(100.dp)) // Padding dưới để không bị nút đè
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.product.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(16.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.product.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
            Text(item.product.description, color = TextSecondary, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Text("$ ${item.product.price}", color = CoffeeOrange, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        // Cụm nút tăng giảm số lượng
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(28.dp).border(1.dp, BorderColor, RoundedCornerShape(20.dp)).clickable { onDecrease() }, Alignment.Center) {
                Icon(Icons.Default.Remove, null, Modifier.size(16.dp), tint = TextSecondary)
            }
            Text("${item.quantity}", Modifier.padding(horizontal = 12.dp), fontWeight = FontWeight.Bold, color = TextPrimary)
            Box(Modifier.size(28.dp).background(CoffeeOrange, RoundedCornerShape(20.dp)).clickable { onIncrease() }, Alignment.Center) {
                Icon(Icons.Default.Add, null, Modifier.size(16.dp), tint = Color.White)
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = if (isTotal) TextPrimary else TextSecondary, fontSize = if (isTotal) 18.sp else 14.sp, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
        Text(value, color = if (isTotal) CoffeeOrange else TextPrimary, fontSize = if (isTotal) 18.sp else 14.sp, fontWeight = FontWeight.Bold)
    }
}