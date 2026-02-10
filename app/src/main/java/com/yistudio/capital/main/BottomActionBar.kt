package com.yistudio.capital.main
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yistudio.capital.utils.formatMoney

@Composable
fun BottomActionBar(
    modifier: Modifier = Modifier,
    offlineEarnings: Double?,
    onClaimOffline: () -> Unit,
    onActivateBoost: () -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 16.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Boost button
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                onClick = onActivateBoost
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "⚡ Boost x5",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "30s income burst",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Offline Claim button
            if (offlineEarnings != null && offlineEarnings > 0.0) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    onClick = onClaimOffline,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Claim",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = formatMoney(offlineEarnings),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    onClick = { /* maybe show tooltip "No offline earnings" */ }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No Offline",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = "Come back later",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}