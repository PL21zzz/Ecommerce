package com.ai.ecommerce.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier
) {
    // Khối tổng của 1 thẻ sản phẩm: Nền trắng (Surface), bo góc 16.dp
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        // 1. Khu vực Ảnh Sản Phẩm + Tag Đánh giá sao đè lên góc trái
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Ép ảnh thành hình vuông chuẩn
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                contentScale = ContentScale.Crop, // Cắt ảnh vừa vặn khuôn
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)) // Bo góc ảnh
            )

            // Tag Đánh Giá Sao (Ví dụ: ⭐ 4.8) nằm ở góc trái trên cùng của ảnh
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f), // Nền đen mờ xuyên thấu
                        shape = RoundedCornerShape(bottomEnd = 8.dp, topStart = 8.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFBBE21), // Màu vàng của ngôi sao
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = product.rating.rate.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Tên món Coffee (Ví dụ: Caffe Latte) - Dùng màu Nâu đà CoffeeDark toàn cục
        Text(
            text = product.title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )

        // 3. Mô tả phụ (Ví dụ: with Chocolate) - Dùng màu xám nhạt TextSecondary
        Text(
            text = product.description,
            color = Color(0xFF9B9B9B),
            fontSize = 12.sp,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Khối Giá Tiền + Nút Dấu Cộng nằm ngang hàng nhau
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Giá tiền hình đô-la ($ 4.53)
            Text(
                text = "$ ${product.price}",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Nút Dấu Cộng màu cam đất (CoffeeOrange)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary, // Tự ăn màu cam đất
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { /* Sau này xử lý thêm vào giỏ hàng ở đây */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add to cart",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}