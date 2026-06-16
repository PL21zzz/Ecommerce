package com.ai.ecommerce.presentation.activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.ui.theme.BorderColor
import com.ai.ecommerce.ui.theme.SurfaceLight
import com.ai.ecommerce.ui.theme.TextSecondary

@Composable
fun SettingsGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceLight, RoundedCornerShape(16.dp))
                .padding(vertical = 4.dp),
            content = content
        )
    }
}