package com.yistudio.capital.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yistudio.capital.ui.state.TaskUiState
import com.yistudio.capital.ui.theme.Dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Sprints", "Milestones", "Moonshots")

    Scaffold(
        topBar = { RoadmapHeader() },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Summary Row (Align with AdventureScreen TopHeader style)
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Q1 GROWTH OBJECTIVES",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        SummaryCard(
                            label = "Current Series",
                            value = "Series B",
                            icon = Icons.Default.Star,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                    item {
                        val totalTasks = uiState.sprintTasks.size + uiState.milestoneTasks.size + uiState.moonshotTasks.size
                        val completedTasks = (uiState.sprintTasks + uiState.milestoneTasks + uiState.moonshotTasks).count { it.status.name == "CLAIMED" }
                        val rate = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f
                        
                        SummaryProgressCard(
                            label = "Completion Rate",
                            progress = rate,
                            icon = Icons.Default.CheckCircle
                        )
                    }
                }
            }

            // Navigation Tabs
            SecondaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { 
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Task List
            val currentTasks = when (selectedTabIndex) {
                0 -> uiState.sprintTasks
                1 -> uiState.milestoneTasks
                else -> uiState.moonshotTasks
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(currentTasks, key = { it.id }) { task ->
                    TaskCard(task = task, onClaim = viewModel::onClaimReward)
                }
            }
        }
    }
}

@Composable
fun RoadmapHeader() {
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
            Text(
                text = "THE BOARDROOM",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Strategic Roadmap",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
            )
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String, icon: ImageVector, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color,
        modifier = Modifier.width(160.dp).height(48.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = label.uppercase(), 
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SummaryProgressCard(label: String, progress: Float, icon: ImageVector) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        modifier = Modifier.width(180.dp).height(48.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(28.dp)) {
                CircularProgressIndicator(
                    progress = { progress },
                    strokeWidth = 3.dp,
                    trackColor = Color.White.copy(alpha = 0.3f),
                    color = Dark
                )
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = label.uppercase(), 
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${(progress * 100).toInt()}% Roadmap", 
                        style = MaterialTheme.typography.bodyMedium, 
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
