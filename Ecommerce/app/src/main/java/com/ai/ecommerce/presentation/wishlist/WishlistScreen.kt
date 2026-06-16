package com.ai.ecommerce.presentation.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ai.ecommerce.presentation.cart.CartViewModel
import com.ai.ecommerce.presentation.home.ProductItem
import com.ai.ecommerce.presentation.home.components.CategoryChips
import com.ai.ecommerce.presentation.wishlist.components.SuggestionRow
import com.ai.ecommerce.ui.theme.BackgroundLight
import com.ai.ecommerce.ui.theme.TextPrimary

@Composable
fun WishlistScreen(
    wishlistViewModel: WishlistViewModel,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val items = wishlistViewModel.wishlistItems

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(BackgroundLight).padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBackIosNew, "Back", Modifier.clickable { navController.popBackStack() }, tint = TextPrimary)
                Text("Wishlist", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Icon(Icons.Default.Search, "Search", tint = TextPrimary)
            }
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tái sử dụng thanh chọn danh mục CategoryChips từ trang Home
            item(span = { GridItemSpan(2) }) {
                CategoryChips()
            }

            // Đổ danh sách các món đã thả tim
            items(items) { product ->
                ProductItem(
                    product = product,
                    isFavorited = true, // Vì nằm trong trang Wishlist nên mặc định là true
                    onAddClick = { cartViewModel.addToCart(product) },
                    onFavoriteClick = { wishlistViewModel.toggleWishlist(product) }, // Bấm để bỏ thích
                    modifier = Modifier.clickable {
                        navController.navigate("detail/${product.id}")
                    }
                )
            }

            // Đắp thêm cụm gợi ý món ăn kèm dưới đáy
            item(span = { GridItemSpan(2) }) {
                SuggestionRow()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}