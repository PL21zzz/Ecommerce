package com.ai.ecommerce.presentation.cart.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = BorderColor)
        SummaryRow("Total", "$ ${String.format("%.2f", total)}", isTotal = true)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = if (isTotal) TextPrimary else TextSecondary, fontSize = if (isTotal) 18.sp else 14.sp, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
        Text(value, color = if (isTotal) CoffeeOrange else TextPrimary, fontSize = if (isTotal) 18.sp else 14.sp, fontWeight = FontWeight.Bold)
    }
}