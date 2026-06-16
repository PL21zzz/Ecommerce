package com.ai.ecommerce.presentation.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.ai.ecommerce.presentation.cart.CartItem
import com.ai.ecommerce.ui.theme.BorderColor
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.TextPrimary
import com.ai.ecommerce.ui.theme.TextSecondary

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.product.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.product.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
            Text(item.product.description, color = TextSecondary, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Text("$ ${item.product.price}", color = CoffeeOrange, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
                    .clickable { onDecrease() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Remove, null, Modifier.size(16.dp), tint = TextSecondary)
            }
            Text("${item.quantity}", Modifier.padding(horizontal = 12.dp), fontWeight = FontWeight.Bold, color = TextPrimary)
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(CoffeeOrange, RoundedCornerShape(20.dp))
                    .clickable { onIncrease() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, null, Modifier.size(16.dp), tint = Color.White)
            }
        }
    }
}