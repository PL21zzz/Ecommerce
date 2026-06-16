package com.ai.ecommerce.presentation.activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.SurfaceLight
import com.ai.ecommerce.ui.theme.TextPrimary
import com.ai.ecommerce.ui.theme.TextSecondary

@Composable
fun UserProfileCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = SurfaceLight,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Khối Avatar có nút chỉnh sửa nhỏ bám góc
            Box(modifier = Modifier.size(100.dp)) {
                Image(
                    painter = rememberAsyncImagePainter("https://res.cloudinary.com/dypm5avrx/image/upload/v1781580379/pedri_qrm4sh.jpg"),
                    contentDescription = "User Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(CoffeeOrange, CircleShape)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Nguyễn Tuấn Phong", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = "Premium Member", fontSize = 14.sp, color = TextSecondary)

            Spacer(modifier = Modifier.height(20.dp))

            // Khối hiển thị 3 cột chỉ số: Orders, Points, Coupons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatColumn(value = "12", label = "Orders")
                Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFEAEAEA)))
                StatColumn(value = "2.4k", label = "Points")
                Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFEAEAEA)))
                StatColumn(value = "3", label = "Coupons")
            }
        }
    }
}

@Composable
private fun StatColumn(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeOrange)
        Text(text = label, fontSize = 13.sp, color = TextSecondary)
    }
}