package com.ai.ecommerce.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ai.ecommerce.presentation.activity.ActivityScreen
import com.ai.ecommerce.presentation.activity.MyOrdersScreen
import com.ai.ecommerce.presentation.auth.AuthScreen
import com.ai.ecommerce.presentation.auth.AuthViewModel
import com.ai.ecommerce.presentation.cart.CartScreen
import com.ai.ecommerce.presentation.cart.CartViewModel
import com.ai.ecommerce.presentation.home.HomeScreen
import com.ai.ecommerce.presentation.home.ProductListViewModel
import com.ai.ecommerce.presentation.product_detail.ProductDetailScreen
import com.ai.ecommerce.presentation.wishlist.WishlistScreen
import com.ai.ecommerce.presentation.wishlist.WishlistViewModel
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.TextSecondary

@Composable
fun MainScreen(
    productViewModel: ProductListViewModel,
    cartViewModel: CartViewModel,
    wishlistViewModel: WishlistViewModel,
    authViewModel: AuthViewModel,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val currentUser = authViewModel.currentUser.value

    if (currentUser == null) {
        AuthScreen(
            uiState = authViewModel.uiState.value,
            onLogin = authViewModel::login,
            onRegister = authViewModel::register
        )
        return
    }

    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Wishlist, Screen.Cart, Screen.Activity)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route

                items.forEach { screen ->
                    val selected = currentRoute == screen.route

                    NavigationBarItem(
                        icon = {
                            if (screen is Screen.Cart) {
                                val cartCount = cartViewModel.totalItemsCount

                                BadgedBox(
                                    badge = {
                                        if (cartCount > 0) {
                                            Badge(
                                                containerColor = CoffeeOrange,
                                                contentColor = Color.White
                                            ) {
                                                Text(text = cartCount.toString(), fontSize = 10.sp)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.title,
                                        tint = if (selected) CoffeeOrange else TextSecondary
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    tint = if (selected) CoffeeOrange else TextSecondary
                                )
                            }
                        },
                        label = { Text(text = screen.title, color = if (selected) CoffeeOrange else TextSecondary) },
                        selected = selected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    wishlistViewModel = wishlistViewModel,
                    navController = navController
                )
            }
            composable(Screen.Wishlist.route) {
                WishlistScreen(
                    productListViewModel = productViewModel,
                    wishlistViewModel = wishlistViewModel,
                    cartViewModel = cartViewModel,
                    navController = navController
                )
            }
            composable(
                route = "detail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                ProductDetailScreen(
                    productId = productId,
                    viewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    wishlistViewModel = wishlistViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    viewModel = cartViewModel,
                    currentUser = currentUser,
                    onBackClick = { navController.popBackStack() },
                    onGoShoppingClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.Activity.route) {
                ActivityScreen(
                    navController = navController,
                    currentUser = currentUser,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = onToggleDarkMode,
                    onLogout = {
                        cartViewModel.clearCart()
                        authViewModel.logout()
                    }
                )
            }
            composable("my_orders") {
                MyOrdersScreen(
                    userId = currentUser.id,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
