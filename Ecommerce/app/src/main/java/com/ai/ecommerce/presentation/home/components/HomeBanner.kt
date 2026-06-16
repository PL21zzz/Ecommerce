package com.ai.ecommerce.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.rememberAsyncImagePainter
import com.ai.ecommerce.ui.theme.CoffeeDark

@Composable
fun HomeBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(CoffeeDark, shape = RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://cdn.pixabay.com/photo/2016/11/19/12/54/beverage-1839134_1280.jpg"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFED5151), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "Promo", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Column {
                Text(text = "Buy one get", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "one FREE", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}