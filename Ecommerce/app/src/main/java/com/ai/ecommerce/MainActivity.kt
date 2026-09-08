package com.ai.ecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ai.ecommerce.presentation.MainScreen
import com.ai.ecommerce.presentation.auth.AuthViewModel
import com.ai.ecommerce.presentation.cart.CartViewModel
import com.ai.ecommerce.presentation.home.ProductListViewModel
import com.ai.ecommerce.presentation.wishlist.WishlistViewModel
import com.ai.ecommerce.ui.theme.EcommerceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ProductListViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val wishlistViewModel: WishlistViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

            EcommerceTheme(darkTheme = isDarkMode) {
                MainScreen(
                    productViewModel = viewModel,
                    cartViewModel = cartViewModel,
                    wishlistViewModel = wishlistViewModel,
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { isDarkMode = it }
                )
            }
        }
    }
}
