package com.ai.ecommerce.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import com.ai.ecommerce.domain.model.Product
import com.ai.ecommerce.ui.theme.*

@Composable
fun ProductItem(
    product: Product,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorited: Boolean = false,          // Thêm trạng thái tim
    showFavoriteIcon: Boolean = true,     // Cho phép ẩn/hiện nút tim tùy màn hình
    onFavoriteClick: () -> Unit = {}       // Sự kiện khi bấm vào tim
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceLight
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Khối chứa ảnh và nút Trái Tim đè lên trên
            Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                AsyncImage(
                    model = product.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp))
                )

                // Điểm số Sao bám vào góc trái trên ảnh giống ảnh mẫu
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color(0x99000000), RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, null, tint = RatingYellow, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(product.rating.rate.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                // ĐÂY RỒI: Nút Trái Tim bám ở góc phải trên ảnh
                if (showFavoriteIcon) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(28.dp)
                            .background(Color.White.copy(alpha = 0.8f), CircleShape)
                            .clickable { onFavoriteClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorited) CoffeeOrange else TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(product.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
            Text(product.description, color = TextSecondary, fontSize = 12.sp)

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$ ${product.price}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(CoffeeOrange, RoundedCornerShape(8.dp))
                        .clickable { onAddClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}