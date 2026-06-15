package com.ai.ecommerce.presentation.product_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ai.ecommerce.presentation.home.ProductListState
import com.ai.ecommerce.presentation.home.ProductListViewModel

@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: ProductListViewModel,
    onBackClick: () -> Unit
) {
    // Tìm đúng sản phẩm trong ViewModel dựa vào productId truyền sang
    val state = viewModel.state.value
    if (state !is ProductListState.Success) return
    val product = state.products.find { it.id == productId } ?: return

    var selectedSize by remember { mutableStateOf("M") } // Trạng thái chọn Size S, M, L

    Scaffold(
        // 1. HEADER PHỤ: Nút quay lại và nút Thả tim
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9F9F9))
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color(0xFF2F2D2C),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() }
                )
                Text(text = "Detail", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2F2D2C))
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color(0xFF2F2D2C),
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        // 5. FOOTER CHI TIẾT: Hiển thị giá và Nút "Buy Now" to đùng
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = Color.White,
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
                        Text(text = "Price", color = Color(0xFF9B9B9B), fontSize = 14.sp)
                        Text(text = "$ ${product.price}", color = Color(0xFFC67C4E), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { /* Xử lý mua ngay */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC67C4E)),
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
    ) { paddingValues ->
        // KHU VỰC THÂN MÀN HÌNH CHỨA RUỘT BIẾN ĐỔI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 2. ẢNH LY COFFEE TO VẬT VÃ
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. THÔNG TIN TIÊU ĐỀ + ĐÁNH GIÁ
            Text(text = product.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2F2D2C))
            Text(text = product.description, fontSize = 12.sp, color = Color(0xFF9B9B9B))

            Row(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = Color(0xFFFBBE21), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = product.rating.rate.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2F2D2C))
                Text(text = " (${product.rating.count})", color = Color(0xFF9B9B9B), fontSize = 14.sp)
            }

            Divider(color = Color(0xFFEAEAEA), thickness = 1.dp)

            // PHẦN MÔ TẢ (Description)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Description", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2F2D2C))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A premium blend coffee crafted from high-quality beans, delivering a rich and velvety texture that satisfies your caffeine cravings with a perfect balanced aroma.",
                fontSize = 14.sp,
                color = Color(0xFF9B9B9B),
                lineHeight = 20.sp
            )

            // 4. KHỐI CHỌN SIZE (S, M, L)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Size", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2F2D2C))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("S", "M", "L").forEach { size ->
                    val isSelected = size == selectedSize
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                color = if (isSelected) Color(0xFFFFF5EE) else Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedSize = size }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Vẽ viền ngoài: Nếu được chọn thì viền màu Cam đất, ngược lại viền xám nhạt
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) Color(0xFFC67C4E) else Color(0xFFEAEAEA)
                            ),
                            color = Color.Transparent,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = size,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFFC67C4E) else Color(0xFF2F2D2C),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}