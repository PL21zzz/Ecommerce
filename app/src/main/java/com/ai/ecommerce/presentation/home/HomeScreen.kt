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
import com.ai.ecommerce.ui.theme.BackgroundLight
import com.ai.ecommerce.ui.theme.CoffeeOrange

@Composable
fun HomeScreen(
    viewModel: ProductListViewModel,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val state = viewModel.state.value

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
                    // Gọi các Component độc lập từ thư mục components ra lắp ráp
                    item(span = { GridItemSpan(2) }) { HomeHeader() }
                    item(span = { GridItemSpan(2) }) { HomeBanner() }
                    item(span = { GridItemSpan(2) }) { CategoryChips() }

                    items(state.products) { product ->
                        ProductItem(
                            product = product,
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