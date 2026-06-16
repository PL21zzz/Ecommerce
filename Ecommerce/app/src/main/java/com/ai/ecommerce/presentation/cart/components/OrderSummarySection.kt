package com.ai.ecommerce.presentation.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.ui.theme.*

@Composable
fun OrderSummarySection(
    subtotal: Double,
    deliveryFee: Double,
    total: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text("Order Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

        SummaryRow("Subtotal", "$ ${String.format("%.2f", subtotal)}")
        SummaryRow("Delivery Fee", "$ ${String.format("%.2f", deliveryFee)}")
        Divider(Modifier.padding(vertical = 12.dp), color = BorderColor)
        SummaryRow("Total", "$ ${String.format("%.2f", total)}", isTotal = true)

        Spacer(modifier = Modifier.height(16.dp))

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
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = if (isTotal) TextPrimary else TextSecondary, fontSize = if (isTotal) 18.sp else 14.sp, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
        Text(value, color = if (isTotal) CoffeeOrange else TextPrimary, fontSize = if (isTotal) 18.sp else 14.sp, fontWeight = FontWeight.Bold)
    }
}