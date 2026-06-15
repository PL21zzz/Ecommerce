package com.ai.ecommerce.presentation

import android.R.attr.type
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ai.ecommerce.presentation.cart.CartScreen
import com.ai.ecommerce.presentation.cart.CartViewModel
import com.ai.ecommerce.presentation.home.HomeScreen
import com.ai.ecommerce.presentation.home.ProductListViewModel
import com.ai.ecommerce.presentation.product_detail.ProductDetailScreen
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.TextSecondary

@Composable
fun MainScreen(
    productViewModel: ProductListViewModel,
    cartViewModel: CartViewModel
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
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                        label = { Text(text = screen.title) },
                        selected = currentRoute == screen.route, // Nếu đúng route thì sáng lên
                        onClick = {
                            if (currentRoute != screen.route) {
                                // Lệnh điều hướng nhảy màn hình của Google Navigation
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoffeeOrange,  // Icon Active ăn màu cam đất Coffee
                            selectedTextColor = CoffeeOrange,  // Chữ Active ăn màu cam đất Coffee
                            unselectedIconColor = TextSecondary,// Icon chưa chọn màu xám nhạt
                            unselectedTextColor = TextSecondary,// Chữ chưa chọn màu xám nhạt
                            indicatorColor = Color.Transparent       // Tắt cái vệt nền tròn mặc định của Google đi cho sạch
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        // 3. VÙNG RUỘT THAY ĐỔI (NavHost): Nơi hoán đổi giao diện dựa trên route
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route, // Mặc định mở app là vào trang Home
            modifier = Modifier.padding(paddingValues) // Ép cái ruột không được đè lên Footer
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = productViewModel,
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
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Tạm thời các trang khác để trống, làm cái text giữ chỗ, hôm sau code cụ thể từng trang
            composable(Screen.Wishlist.route) {
                Text(text = "Màn hình Yêu Thích (Đang phát triển)")
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    viewModel = cartViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.Activity.route) {
                Text(text = "Màn hình Lịch sử/Thông báo (Đang phát triển)")
            }
        }
    }
}