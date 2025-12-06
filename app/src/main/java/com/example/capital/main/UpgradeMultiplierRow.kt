package com.example.capital.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.capital.model.SpeedMultiplier

@Composable
fun UpgradeMultiplierRow(
    selectedMultiplier: SpeedMultiplier,
    onSelect: (SpeedMultiplier) -> Unit,
    modifier: Modifier = Modifier
) {
    val options: List<SpeedMultiplier> = listOf(SpeedMultiplier.One(), SpeedMultiplier.Ten(), SpeedMultiplier.TwentyFive(), SpeedMultiplier.Hundred(), SpeedMultiplier.Max())

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEach { label ->
            val isSelected = label == selectedMultiplier
            OutlinedButton(
                onClick = { onSelect(label) },
                modifier = Modifier.weight(1f),
                colors = if (isSelected) {
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors()
                },
                border = if (isSelected) null else ButtonDefaults.outlinedButtonBorder
            ) {
                Text(
                    text = label.,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                )
            }
        }
    }
}

