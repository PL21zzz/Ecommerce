package com.ai.ecommerce.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.ui.theme.BackgroundLight
import com.ai.ecommerce.ui.theme.CoffeeDark
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.TextPrimary
import com.ai.ecommerce.ui.theme.TextSecondary

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(text = "Location", color = TextSecondary, fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Jakarta, Indonesia",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select Location",
                tint = TextPrimary,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Search coffee...", color = TextSecondary, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(CoffeeOrange, shape = RoundedCornerShape(12.dp))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filter",
                    tint = Color.White
                )
            }
        }
    }
}