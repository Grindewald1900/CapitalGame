package com.yistudio.capital.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import com.yistudio.domain.entity.LevelMultiplier

@Composable
fun UpgradeMultiplierRow(
    selectedMultiplier: LevelMultiplier,
    onSelect: (LevelMultiplier) -> Unit,
    modifier: Modifier = Modifier
) {
    val options: List<LevelMultiplier> = listOf(
        LevelMultiplier.One,
        LevelMultiplier.Ten,
        LevelMultiplier.TwentyFive,
        LevelMultiplier.Hundred,
        LevelMultiplier.Max
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEach { label ->
            // Use class comparison for reliability across layers
            val isSelected = label::class == selectedMultiplier::class
            
            OutlinedButton(
                onClick = { onSelect(label) },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(),
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
                    text = label.str,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                )
            }
        }
    }
}
