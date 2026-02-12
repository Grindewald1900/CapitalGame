package com.yistudio.capital.managers

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yistudio.capital.ui.theme.Golden
import com.yistudio.capital.utils.formatMoney
import com.yistudio.domain.entity.IndustryTag
import com.yistudio.domain.entity.ManagerEntity
import com.yistudio.domain.entity.ManagerRarity

// Define Custom Colors
val SlateBlue = Color(0xFF53668E)
val GlassBackground = Color(0xE61A237E) // Semi-transparent Dark Blue

@Composable
fun ManagersScreen(
    viewModel: ManagersViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5F7))
    ) {
        // 1. Header: Strategic Oversight
        StrategicHeader(state.totalYield, state.luckTriggered, state.activeManagers.size, state.totalSlots)

        Spacer(modifier = Modifier.height(16.dp))

        // 2. The Board (Horizontal Row)
        BoardSection(
            managers = state.activeManagers,
            totalSlots = state.totalSlots,
            onSwap = viewModel::swapManagers
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Talent Marketplace
        Text(
            text = "TALENT ACQUISITION",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.marketplace) { manager ->
                MarketplaceCard(
                    manager = manager, 
                    onRecruit = { viewModel.recruit(manager) },
                    canAfford = if (manager.costType == "CASH") state.cash >= manager.costAmount else state.equity >= manager.costAmount
                )
            }
        }
    }
}

@Composable
fun StrategicHeader(yield: Double, isLuck: Boolean, filled: Int, total: Int) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STRATEGIC OVERSIGHT",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Board Seats: $filled/$total",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val scale by if (isLuck) {
                val infiniteTransition = rememberInfiniteTransition()
                infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(300),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            } else {
                remember { mutableStateOf(1f) }
            }

            Text(
                text = "x${String.format("%,.2f", yield)} Global Yield",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isLuck) Golden else MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.scale(scale)
            )
        }
    }
}

@Composable
fun BoardSection(
    managers: List<ManagerEntity>,
    totalSlots: Int,
    onSwap: (Int, Int) -> Unit
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(totalSlots) { index ->
            val manager = managers.getOrNull(index)
            BoardSlotCard(
                manager = manager,
                index = index,
                isSelected = selectedIndex == index,
                hasLeftSynergy = index > 0 && manager != null && managers.getOrNull(index - 1)?.industry == manager.industry,
                onClick = {
                    if (selectedIndex == null) {
                        if (manager != null) selectedIndex = index
                    } else {
                        onSwap(selectedIndex!!, index)
                        selectedIndex = null
                    }
                }
            )
        }
    }
}

@Composable
fun BoardSlotCard(
    manager: ManagerEntity?,
    index: Int,
    isSelected: Boolean,
    hasLeftSynergy: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Golden else if (hasLeftSynergy) Golden else Color.Transparent
    
    Box(
        modifier = Modifier.width(140.dp)
    ) {
        // Synergy Line (Left)
        if (hasLeftSynergy) {
            Box(
                modifier = Modifier
                    .width(12.dp)
                    .height(2.dp)
                    .background(Golden)
                    .align(Alignment.CenterStart)
                    .offset(x = (-12).dp)
            )
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (manager != null) SlateBlue else Color.White,
            border = BorderStroke(2.dp, borderColor),
            shadowElevation = 4.dp,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            if (manager != null) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Portrait Placeholder
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(GlassBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = manager.name.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = manager.title,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = manager.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    IndustryBadge(manager.industry)
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = "x${manager.baseMultiplier}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Golden,
                        fontWeight = FontWeight.Black
                    )
                }
            } else {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.LightGray)
                        Text("Empty", color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun MarketplaceCard(
    manager: ManagerEntity,
    onRecruit: () -> Unit,
    canAfford: Boolean
) {
    val isLegendary = manager.rarity == ManagerRarity.LEGENDARY
    val cardColor = if (isLegendary) Color(0xFF2C3E50) else Color.White
    val border = if (isLegendary) BorderStroke(1.dp, Golden) else null

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = cardColor,
        border = border,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SlateBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(manager.name.take(1), color = Color.White, fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = manager.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isLegendary) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IndustryBadge(manager.industry, small = true)
                }
                Text(
                    text = "x${manager.baseMultiplier} Yield • x${manager.synergyMultiplier} Synergy",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isLegendary) Color.White.copy(alpha = 0.7f) else Color.Gray
                )
            }
            
            Button(
                onClick = onRecruit,
                enabled = canAfford,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (manager.costType == "EQUITY") Golden else MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (manager.costType == "EQUITY") "📈 ${manager.costAmount.toInt()}" else formatMoney(manager.costAmount),
                    color = if (manager.costType == "EQUITY") Color.Black else Color.White
                )
            }
        }
    }
}

@Composable
fun IndustryBadge(tag: IndustryTag, small: Boolean = false) {
    val color = when(tag) {
        IndustryTag.TECH -> Color(0xFF00BFA5) // Teal
        IndustryTag.FINANCE -> Color(0xFF2962FF) // Blue
        IndustryTag.FOOD -> Color(0xFFFFA000) // Orange
        IndustryTag.ENERGY -> Color(0xFFFFD600) // Yellow
        IndustryTag.RETAIL -> Color(0xFFD500F9) // Purple
    }
    
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = tag.name,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = if (small) 8.sp else 10.sp),
            color = color.copy(alpha = 1f) // Make text fully opaque for readability
        )
    }
}
