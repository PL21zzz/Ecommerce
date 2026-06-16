package com.ai.ecommerce.presentation

import android.R.attr.type
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
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
import com.ai.ecommerce.presentation.cart.CartScreen
import com.ai.ecommerce.presentation.cart.CartViewModel
import com.ai.ecommerce.presentation.home.HomeScreen
import com.ai.ecommerce.presentation.home.ProductListViewModel
import com.ai.ecommerce.presentation.product_detail.ProductDetailScreen
import com.ai.ecommerce.presentation.wishlist.WishlistViewModel
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.TextSecondary

@Composable
fun MainScreen(
    productViewModel: ProductListViewModel,
    cartViewModel: CartViewModel,
    wishlistViewModel: WishlistViewModel
) {
    // 1. Khởi tạo bộ điều khiển chuyển cảnh (NavController)
    val navController = rememberNavController()

    // Danh sách 4 nút bấm dưới Footer tương ứng với 4 màn hình
    val items = listOf(Screen.Home, Screen.Wishlist, Screen.Cart, Screen.Activity)

    // 2. Dùng Scaffold để dựng Giàn Giáo giữ cố định Footer (bottomBar)
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White // Nền trắng tinh cho Footer giống ảnh mẫu
            ) {
                // Lấy thông tin xem hiện tại người dùng đang đứng ở màn hình nào
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route

                items.forEach { screen ->
                    val selected = currentRoute == screen.route

                    NavigationBarItem(
                        icon = {
                            // Kiểm tra xem icon hiện tại có phải là icon Giỏ hàng hay không
                            if (screen is Screen.Cart) {
                                // Lấy tổng số lượng sản phẩm thật từ ViewModel lên
                                val cartCount = cartViewModel.totalItemsCount

                                BadgedBox(
                                    badge = {
                                        // Chỉ hiển thị số khi trong giỏ hàng thực sự có đồ (lớn hơn 0)
                                        if (cartCount > 0) {
                                            Badge(
                                                containerColor = CoffeeOrange, // Cho ăn theo màu cam đất chuẩn của bạn
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
                                // Các icon khác (Home, Likes, Activity) thì vẽ bình thường không cần Badge
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
        // 3. VÙNG RUỘT THAY ĐỔI (NavHost): Nơi hoán đổi giao diện dựa trên route
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // home
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    wishlistViewModel = wishlistViewModel, // <-- Truyền sang cho Home dùng
                    navController = navController
                )
            }
            // wishlist
            composable(Screen.Wishlist.route) {
                com.ai.ecommerce.presentation.wishlist.WishlistScreen(
                    wishlistViewModel = wishlistViewModel,
                    cartViewModel = cartViewModel,
                    navController = navController
                )
            }
            // product detail
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
            // cart
            composable(Screen.Cart.route) {
                CartScreen(
                    viewModel = cartViewModel,
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
            // activity
            composable(Screen.Activity.route) {
                ActivityScreen()
            }
        }
    }
}