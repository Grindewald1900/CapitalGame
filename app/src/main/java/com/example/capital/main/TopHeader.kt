package com.example.capital.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capital.ui.theme.Golden
import com.example.capital.utils.formatMoney
import com.example.domain.entity.LevelMultiplier

@Composable
fun TopHeader(
    cash: Double,
    incomePerSec: Double,
    influence: Double,
    equity: Double,
    prestigePoints: Int,
    selectedMultiplier: LevelMultiplier,
    onOpenPrestige: () -> Unit,
    onSpeedSelected: (speed: LevelMultiplier) -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Main Cash and Income Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "TOTAL CAPITAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    AnimatedNumberTicker(
                        text = formatMoney(cash),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "INCOME",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    AnimatedNumberTicker(
                        text = "+${formatMoney(incomePerSec)}/s",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50) // Material Green
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary Currencies Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurrencyChip(
                    label = "Influence",
                    value = influence,
                    icon = "⭐",
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.weight(1f)
                )
                CurrencyChip(
                    label = "Equity",
                    value = equity,
                    icon = "📈",
                    color = Golden,
                    modifier = Modifier.weight(1f)
                )
                
                // Prestige Button
                FilledTonalButton(
                    onClick = onOpenPrestige,
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "PRESTIGE",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$prestigePoints 🔆",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multiplier selector
            UpgradeMultiplierRow(
                selectedMultiplier = selectedMultiplier,
                onSelect = onSpeedSelected
            )
        }
    }
}

@Composable
private fun CurrencyChip(
    label: String,
    value: Double,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(text = icon, modifier = Modifier.padding(end = 4.dp))
            Column {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AnimatedNumberTicker(
                    text = formatMoney(value).removePrefix("$"),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun AnimatedNumberTicker(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        text.forEach { char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInVertically { it } + fadeIn()).togetherWith(slideOutVertically { -it } + fadeOut())
                    } else {
                        (slideInVertically { -it } + fadeIn()).togetherWith(slideOutVertically { it } + fadeOut())
                    }.using(SizeTransform(clip = false))
                },
                label = "DigitTicker"
            ) { animatedChar ->
                Text(
                    text = animatedChar.toString(),
                    style = style,
                    softWrap = false,
                    maxLines = 1
                )
            }
        }
    }
}
