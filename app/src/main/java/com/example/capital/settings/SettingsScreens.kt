package com.example.capital.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.capital.R

@Composable
fun InvestorsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Investors", style = MaterialTheme.typography.headlineMedium)
            Text("Prestige and earn Angel Investors", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Coming Soon...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SettingsScreen() {
    val settingsItems = listOf(
        SettingsItem("Languages", R.drawable.baseline_task_24),
        SettingsItem("Account", R.drawable.baseline_task_24),
        SettingsItem("Notification", R.drawable.baseline_task_24),
        SettingsItem("Audio & Visual", R.drawable.baseline_task_24),
        SettingsItem("Gameplay", R.drawable.baseline_task_24),
        SettingsItem("Theme",R.drawable.baseline_task_24),
        SettingsItem("Support", R.drawable.baseline_task_24)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(settingsItems) { item ->
                ListItem(
                    headlineContent = { Text(item.title) },
                    leadingContent = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id =item.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id =item.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.clickable { /* Handle click */ }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

private data class SettingsItem(
    val title: String,
    val icon: Int
)
