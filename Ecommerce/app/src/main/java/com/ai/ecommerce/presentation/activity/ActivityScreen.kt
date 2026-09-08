package com.ai.ecommerce.presentation.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ai.ecommerce.presentation.activity.components.SettingRowItem
import com.ai.ecommerce.presentation.activity.components.SettingsGroup
import com.ai.ecommerce.presentation.activity.components.UserProfileCard
import com.ai.ecommerce.presentation.auth.AuthUser
import com.ai.ecommerce.ui.theme.BorderColor
import com.ai.ecommerce.ui.theme.TextPrimary

@Composable
fun ActivityScreen(
    navController: NavController,
    currentUser: AuthUser,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Place, contentDescription = null, tint = TextPrimary)
                Text("Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Icon(Icons.Default.Search, contentDescription = null, tint = TextPrimary)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserProfileCard(name = currentUser.name, email = currentUser.email)

            SettingsGroup(title = "Account") {
                SettingRowItem(
                    icon = Icons.Default.Person,
                    label = "Personal Info",
                    trailingText = currentUser.email
                )
                HorizontalDivider(color = BorderColor, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingRowItem(
                    icon = Icons.Default.History,
                    label = "Order History",
                    onClick = { navController.navigate("my_orders") }
                )
            }

            SettingsGroup(title = "Preferences") {
                SettingRowItem(icon = Icons.Default.Language, label = "Language", trailingText = "English")
                HorizontalDivider(color = BorderColor, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                SettingRowItem(
                    icon = Icons.Default.DarkMode,
                    label = "Dark Mode",
                    showSwitch = true,
                    switchChecked = isDarkMode,
                    onSwitchChange = { onToggleDarkMode(it) }
                )
            }

            SettingsGroup(title = "Security") {
                SettingRowItem(icon = Icons.Default.Lock, label = "Change Password")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogout() }
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
