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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ai.ecommerce.presentation.cart.CartViewModel
import com.ai.ecommerce.presentation.home.ProductItem
import com.ai.ecommerce.presentation.home.ProductListViewModel
import com.ai.ecommerce.presentation.home.components.CategoryChips
import com.ai.ecommerce.presentation.wishlist.components.SuggestionRow
import com.ai.ecommerce.ui.theme.BackgroundLight
import com.ai.ecommerce.ui.theme.TextPrimary

@Composable
fun WishlistScreen(
    productListViewModel: ProductListViewModel,
    wishlistViewModel: WishlistViewModel,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val items = wishlistViewModel.wishlistItems

    // 2. Tạo một biến trạng thái tạm để quản lý việc lọc danh mục ngay tại bộ nhớ trang Wishlist
    var localSelectedCategoryId by remember { mutableStateOf<Int?>(null) }
    val categoriesList = productListViewModel.categories.value

    // 3. Tự động lọc danh sách sản phẩm đã thích khớp với danh mục đang chọn
    val filteredItems = remember(items, localSelectedCategoryId) {
        if (localSelectedCategoryId == null) {
            items // Nếu chọn All thì hiện hết tất cả đồ đã thả tim
        } else {
            // Lọc đống đồ đã thả tim, lấy những món có tên category khớp với danh mục trong DB
            items.filter { product ->
                val categoryObj = categoriesList.find { it.id == localSelectedCategoryId }
                product.category == categoryObj?.name
            }
        }
    }

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
            // Tái sử dụng thanh chọn danh mục dữ liệu thật không lo lỗi nữa
            item(span = { GridItemSpan(2) }) {
                CategoryChips(
                    categoriesFromDb = categoriesList,
                    selectedId = localSelectedCategoryId,
                    onCategorySelected = { targetId ->
                        localSelectedCategoryId = targetId // Cập nhật ID để bộ lọc phía trên tự nhảy dữ liệu
                    }
                )
            }

            // Đổ danh sách các món ĐÃ ĐƯỢC LỌC mượt mà
            items(filteredItems) { product ->
                ProductItem(
                    product = product,
                    isFavorited = true,
                    onAddClick = { cartViewModel.addToCart(product) },
                    onFavoriteClick = { wishlistViewModel.toggleWishlist(product) },
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