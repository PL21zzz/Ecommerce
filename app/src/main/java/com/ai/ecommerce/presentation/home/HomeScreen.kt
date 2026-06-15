package com.ai.ecommerce.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun HomeScreen(viewModel: ProductListViewModel, navController: NavController) {
    val state = viewModel.state.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9)) // Màu nền xám kem nhạt toàn cục
    ) {
        when (state) {
            is ProductListState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFC67C4E)
                )
            }
            is ProductListState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is ProductListState.Success -> {
                // Dùng LazyVerticalGrid làm trục cuộn dọc chính cho toàn màn hình Home
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Dùng GridItemSpan để ép các hàng Header, Banner, Chips trải dài hết 2 cột
                    item(span = { GridItemSpan(2) }) {
                        HomeHeader()
                    }
                    item(span = { GridItemSpan(2) }) {
                        HomeBanner()
                    }
                    item(span = { GridItemSpan(2) }) {
                        CategoryChips()
                    }

                    // Đây là lưới đổ 20 cái thẻ ly Coffee ra
                    items(state.products) { product ->
                        ProductItem(
                            product = product,
                            modifier = Modifier.clickable {
                                navController.navigate("detail/${product.id}")
                        })
                    }
                }
            }
        }
    }
}

// 1. COMPONENT HEADER (Vị trí + Ô tìm kiếm + Nút lọc)
@Composable
fun HomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Cụm Vị trí
        Text(text = "Location", color = Color(0xFF9B9B9B), fontSize = 12.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Jakarta, Indonesia",
                color = Color(0xFF2F2D2C),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select Location",
                tint = Color(0xFF2F2D2C),
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Thanh Tìm kiếm + Nút lọc nằm ngang hàng
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ô Search bo góc tròn, nền trắng tinh
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
                    tint = Color(0xFF2F2D2C)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Search coffee...", color = Color(0xFF9B9B9B), fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Nút mở bộ lọc màu cam đất (CoffeeOrange)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFC67C4E), shape = RoundedCornerShape(12.dp))
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

// 2. COMPONENT BANNER (Buy one get one FREE)
@Composable
fun HomeBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(Color(0xFF2F2D2C), shape = RoundedCornerShape(16.dp)) // Nền nâu đà
    ) {
        // Ảnh background ly coffee nghệ thuật nằm ẩn góc phải banner
        Image(
            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?q=80&w=400"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
        )

        // Lớp nội dung chữ đè lên bên trái banner
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Tag chữ "Promo" màu đỏ đặc trưng
            Box(
                modifier = Modifier
                    .background(Color(0xFFED5151), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "Promo", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            // Dòng chữ khuyến mãi to đùng trắng tinh
            Column {
                Text(
                    text = "Buy one get",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "one FREE",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// 3. COMPONENT CHIPS (Thanh chọn danh mục cuộn ngang)
@Composable
fun CategoryChips() {
    val categories = listOf("All Coffee", "Macchiato", "Latte", "Americano", "Cappuccino")
    var selectedCategory by remember { mutableStateOf("All Coffee") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()) // Kích hoạt cuộn ngang mượt mà
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) Color(0xFFC67C4E) else Color.White, // Đổi nền theo trạng thái click
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { selectedCategory = category }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = category,
                    color = if (isSelected) Color.White else Color(0xFF2F2D2C), // Đổi màu chữ theo trạng thái click
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}