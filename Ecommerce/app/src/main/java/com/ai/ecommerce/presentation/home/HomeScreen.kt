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
import androidx.compose.runtime.remember
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
    val categoriesList = viewModel.categories.value
    val selectedId = viewModel.selectedCategoryId.value

    // Đọc từ khóa tìm kiếm hiện tại từ ViewModel lên
    val searchQuery = viewModel.searchQuery.value

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
                // Tự động lọc danh sách sản phẩm theo từ khóa tìm kiếm (Không phân biệt chữ hoa/thường)
                val filteredProducts = remember(state.products, searchQuery) {
                    if (searchQuery.isEmpty()) {
                        state.products
                    } else {
                        state.products.filter { product ->
                            product.title.contains(searchQuery, ignoreCase = true)
                        }
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Truyền từ khóa và sự kiện thay đổi văn bản vào HomeHeader
                    item(span = { GridItemSpan(2) }) {
                        HomeHeader(
                            searchQuery = searchQuery,
                            onSearchQueryChange = { text -> viewModel.changeSearchQuery(text) }
                        )
                    }

                    item(span = { GridItemSpan(2) }) { HomeBanner() }

                    item(span = { GridItemSpan(2) }) {
                        CategoryChips(
                            categoriesFromDb = categoriesList,
                            selectedId = selectedId,
                            onCategorySelected = { targetId ->
                                viewModel.fetchProducts(targetId)
                            }
                        )
                    }

                    // Đổi từ state.products sang hiển thị danh sách filteredProducts đã được lọc dữ liệu
                    items(filteredProducts) { product ->
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