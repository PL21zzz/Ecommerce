package com.ai.ecommerce.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    // 1. Tiêu đề lớn (Dùng cho Tên sản phẩm trong trang chi tiết, Tiêu đề màn hình)
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    // 2. Tiêu đề vừa (Dùng cho tên danh mục, tiêu đề các khối block ở trang chủ)
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    // 3. Chữ nội dung lớn (Dùng cho giá tiền hiển thị to, hoặc đoạn mô tả chính)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // 4. Chữ nội dung nhỏ (Dùng cho mô tả phụ, số lượng đã bán, đánh giá star)
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // 5. Nhãn nhỏ (Dùng cho nút bấm bấm, text trên các tag giảm giá % )
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)