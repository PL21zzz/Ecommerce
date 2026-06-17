package com.ai.ecommerce.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ai.ecommerce.presentation.cart.CartViewModel
import com.ai.ecommerce.presentation.home.components.*
import com.ai.ecommerce.presentation.wishlist.WishlistViewModel
import com.ai.ecommerce.ui.theme.BackgroundLight
import com.ai.ecommerce.ui.theme.CoffeeOrange

@Composable
fun HomeScreen(
    viewModel: ProductListViewModel,
    cartViewModel: CartViewModel,
    wishlistViewModel: WishlistViewModel,
    navController: NavController
) {
    val state = viewModel.state.value
    // Lấy danh sách categories và ID đang được chọn từ ViewModel
    val categoriesList = viewModel.categories.value
    val selectedId = viewModel.selectedCategoryId.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        when (state) {
            is ProductListState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = CoffeeOrange
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(span = { GridItemSpan(2) }) { HomeHeader() }
                    item(span = { GridItemSpan(2) }) { HomeBanner() }

                    // Nạp dữ liệu thật và cấu hình lọc sự kiện khi bấm nút
                    item(span = { GridItemSpan(2) }) {
                        CategoryChips(
                            categoriesFromDb = categoriesList,
                            selectedId = selectedId,
                            onCategorySelected = { targetId ->
                                viewModel.fetchProducts(targetId)
                            }
                        )
                    }

                    items(state.products) { product ->
                        ProductItem(
                            product = product,
                            isFavorited = wishlistViewModel.isFavorited(product.id),
                            onFavoriteClick = { wishlistViewModel.toggleWishlist(product) },
                            onAddClick = { cartViewModel.addToCart(product) },
                            modifier = Modifier.clickable {
                                navController.navigate("detail/${product.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}