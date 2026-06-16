package com.ai.ecommerce.presentation.activity.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.ecommerce.ui.theme.CoffeeOrange
import com.ai.ecommerce.ui.theme.TextPrimary
import com.ai.ecommerce.ui.theme.TextSecondary

@Composable
fun SettingRowItem(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    trailingText: String? = null,
    showSwitch: Boolean = false,
    switchChecked: Boolean = false,
    onSwitchChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !showSwitch) { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 15.sp, color = TextPrimary, modifier = Modifier.weight(1f))

        when {
            showSwitch -> {
                Switch(
                    checked = switchChecked,
                    onCheckedChange = onSwitchChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                        checkedTrackColor = CoffeeOrange
                    )
                )
            }
            trailingText != null -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = trailingText, fontSize = 14.sp, color = TextSecondary, modifier = Modifier.padding(end = 4.dp))
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
                }
            }
            else -> {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
            }
        }
    }
}