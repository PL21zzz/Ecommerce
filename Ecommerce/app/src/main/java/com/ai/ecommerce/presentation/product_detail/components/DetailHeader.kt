package com.ai.ecommerce.presentation.product_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.ui.theme.BackgroundLight
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.TextPrimary

@Composable
fun DetailHeader(
    isFavorited: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Back",
            tint = TextPrimary,
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() }
        )
        Text(
            text = "Detail",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Icon(
            imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavorited) CoffeeOrange else TextPrimary,
            modifier = Modifier
                .size(24.dp)
                .clickable { onFavoriteClick() }
        )
    }
}