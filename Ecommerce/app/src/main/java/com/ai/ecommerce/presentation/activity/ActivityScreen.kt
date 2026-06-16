package com.ai.ecommerce.presentation.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.presentation.activity.components.SettingRowItem
import com.ai.ecommerce.presentation.activity.components.SettingsGroup
import com.ai.ecommerce.presentation.activity.components.UserProfileCard
import com.ai.ecommerce.ui.theme.BackgroundLight
import com.ai.ecommerce.ui.theme.BorderColor
import com.ai.ecommerce.ui.theme.TextPrimary

@Composable
fun ActivityScreen() {
    var isDarkMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Place, contentDescription = null, tint = TextPrimary)
                Text("Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Icon(Icons.Default.Search, contentDescription = null, tint = TextPrimary)
            }
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Khối thông tin thẻ cá nhân
            UserProfileCard()

            // 2. Nhóm cấu hình Tài khoản
            SettingsGroup(title = "Account") {
                SettingRowItem(icon = Icons.Default.Person, label = "Personal Info")
                Divider(color = BorderColor, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingRowItem(icon = Icons.Default.History, label = "Order History")
            }

            // 3. Nhóm cấu hình Tùy chọn
            SettingsGroup(title = "Preferences") {
                SettingRowItem(icon = Icons.Default.Language, label = "Language", trailingText = "English")
                Divider(color = BorderColor, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingRowItem(
                    icon = Icons.Default.DarkMode,
                    label = "Dark Mode",
                    showSwitch = true,
                    switchChecked = isDarkMode,
                    onSwitchChange = { isDarkMode = it }
                )
            }

            // 4. Nhóm cấu hình Bảo mật
            SettingsGroup(title = "Security") {
                SettingRowItem(icon = Icons.Default.Lock, label = "Change Password")
            }

            // 5. Nút Đăng xuất màu đỏ nổi bật dưới đáy
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Xử lý Đăng xuất */ }
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = Color(0xFFED5151))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Log Out", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFED5151))
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}