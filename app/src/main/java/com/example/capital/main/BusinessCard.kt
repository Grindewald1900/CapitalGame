package com.example.capital.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.capital.ui.state.BusinessUiState
import com.example.capital.utils.formatMoney


@Composable
fun BusinessCard(
    business: BusinessUiState,
    onUpgrade: () -> Unit
) {
    val isLocked = business.level == 0
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isLocked) 0.6f else 1.0f) // Grey out if locked
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Row 1: Name + Level + Automation badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = business.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = if (isLocked) "Locked" else "Lv ${business.level}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!isLocked) {
                    AutomationBadge(automated = business.automated)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Row 2: Income/sec + Upgrade button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Income/sec",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatMoney(business.incomePerSec) + "/s",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Button(
                    onClick = onUpgrade,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    colors = if (isLocked) {
                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    } else {
                        ButtonDefaults.buttonColors()
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isLocked) "Unlock" else "Upgrade",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = formatMoney(business.upgradeCost),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
