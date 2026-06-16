package com.ai.ecommerce.presentation.product_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ai.ecommerce.presentation.cart.CartViewModel
import com.ai.ecommerce.presentation.home.ProductListState
import com.ai.ecommerce.presentation.home.ProductListViewModel
import com.ai.ecommerce.presentation.wishlist.WishlistViewModel
import com.ai.ecommerce.presentation.product_detail.components.DetailBottomBar
import com.ai.ecommerce.presentation.product_detail.components.DetailHeader
import com.ai.ecommerce.presentation.product_detail.components.SizeSelector
import com.ai.ecommerce.ui.theme.*

@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: ProductListViewModel,
    cartViewModel: CartViewModel,
    wishlistViewModel: WishlistViewModel,
    onBackClick: () -> Unit
) {
    val state = viewModel.state.value
    if (state !is ProductListState.Success) return
    val product = state.products.find { it.id == productId } ?: return

    var selectedSize by remember { mutableStateOf("M") }

    Scaffold(
        // 2. HEADER ĐỘC LẬP: Truyền trạng thái tim và hàm xử lý click vào tim
        topBar = {
            DetailHeader(
                isFavorited = wishlistViewModel.isFavorited(product.id),
                onBackClick = onBackClick,
                onFavoriteClick = { wishlistViewModel.toggleWishlist(product) }
            )
        },
        // 3. BOTTOMBAR ĐỘC LẬP: Truyền giá tiền và hành động bỏ đồ vào giỏ
        bottomBar = {
            DetailBottomBar(
                price = product.price,
                onBuyNowClick = { cartViewModel.addToCart(product) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

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

            Text(text = product.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = product.description, fontSize = 12.sp, color = TextSecondary)

            Row(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = RatingYellow, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = product.rating.rate.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                Text(text = " (${product.rating.count})", color = TextSecondary, fontSize = 14.sp)
            }

            Divider(color = BorderColor, thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Description", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A premium blend coffee crafted from high-quality beans, delivering a rich and velvety texture that satisfies your caffeine cravings with a perfect balanced aroma.",
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. CHỌN SIZE ĐỘC LẬP
            SizeSelector(
                selectedSize = selectedSize,
                onSizeSelected = { selectedSize = it }
            )
        }
    }
}