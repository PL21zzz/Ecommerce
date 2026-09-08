package com.ai.ecommerce.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 1. Màu chủ đạo (Primary)
val CoffeeDark = Color(0xFF2F2D2C)

// 2. Màu nhấn (Secondary/Accent) - Màu cam đất
val CoffeeOrange = Color(0xFFC67C4E)

// 3. Màu nền tổng thể (Background) - Động theo Theme (Trắng khi Light, Đen khi Dark)
val BackgroundLight: Color
    @Composable get() = MaterialTheme.colorScheme.background

// 4. Màu nền của các thẻ Card (Surface) - Động theo Theme (Trắng khi Light, Đen xám khi Dark)
val SurfaceLight: Color
    @Composable get() = MaterialTheme.colorScheme.surface

// 5. Màu chữ chính (TextPrimary) - Động theo Theme (Đen khi Light, Trắng khi Dark)
val TextPrimary: Color
    @Composable get() = MaterialTheme.colorScheme.onBackground

// 6. Màu chữ phụ
val TextSecondary = Color(0xFF9B9B9B)

// 7. Màu đường viền - Động theo Theme
val BorderColor: Color
    @Composable get() = MaterialTheme.colorScheme.outline

val CoffeeOrangeLight = Color(0xFFFFF5EE)
val RatingYellow = Color(0xFFFBBE21)