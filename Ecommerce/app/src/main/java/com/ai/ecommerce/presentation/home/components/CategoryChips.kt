package com.ai.ecommerce.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.presentation.home.JavaCategory
import com.ai.ecommerce.ui.theme.CoffeeDark
import com.ai.ecommerce.ui.theme.CoffeeOrange

@Composable
fun CategoryChips(
    categoriesFromDb: List<JavaCategory>,
    selectedId: Int?,
    onCategorySelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    // Tự tạo một item ảo đại diện cho nút "All" không có trong DB
    val allCategory = JavaCategory(id = -1, name = "All")

    // Gộp nút "All" vào đầu danh sách danh mục đổ từ Java về
    val fullCategoriesList = remember(categoriesFromDb) {
        listOf(allCategory) + categoriesFromDb
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        fullCategoriesList.forEach { category ->
            // Nếu id = -1 và selectedId == null nghĩa là đang chọn nút All
            val isSelected = if (category.id == -1) selectedId == null else category.id == selectedId

            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) CoffeeOrange else Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        // Nếu bấm nút All thì truyền null để lấy hết, ngược lại truyền ID thật
                        val targetId = if (category.id == -1) null else category.id
                        onCategorySelected(targetId)
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = category.name,
                    color = if (isSelected) Color.White else CoffeeDark,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}