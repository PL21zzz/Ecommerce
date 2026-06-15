package com.ai.ecommerce.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.ui.theme.CoffeeDark
import com.ai.ecommerce.ui.theme.CoffeeOrange

@Composable
fun CategoryChips(modifier: Modifier = Modifier) {
    val categories = listOf("All Coffee", "Macchiato", "Latte", "Americano", "Cappuccino")
    var selectedCategory by remember { mutableStateOf("All Coffee") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) CoffeeOrange else Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { selectedCategory = category }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = category,
                    color = if (isSelected) Color.White else CoffeeDark,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}