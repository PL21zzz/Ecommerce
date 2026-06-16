package com.ai.ecommerce.presentation.product_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.SurfaceLight
import com.ai.ecommerce.ui.theme.TextSecondary

@Composable
fun DetailBottomBar(
    price: Double,
    onBuyNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shadowElevation = 8.dp,
        color = SurfaceLight,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Price", color = TextSecondary, fontSize = 14.sp)
                Text(
                    text = "$ $price",
                    color = CoffeeOrange,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = onBuyNowClick,
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeOrange),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
            ) {
                Text(text = "Buy Now", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}