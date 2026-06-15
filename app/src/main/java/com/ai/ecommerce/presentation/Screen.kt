package com.ai.ecommerce.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Wishlist : Screen("wishlist", "Likes", Icons.Default.Favorite)
    object Cart : Screen("cart", "Cart", Icons.Default.ShoppingCart)
    object Activity : Screen("activity", "Activity", Icons.Default.EventNote)
    object ProductDetail : Screen("detail/{productId}", "Detail", Icons.Default.Home)
}