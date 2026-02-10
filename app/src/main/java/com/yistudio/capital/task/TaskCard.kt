package com.yistudio.capital.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yistudio.capital.ui.theme.Dark
import com.yistudio.capital.ui.theme.Golden
import com.yistudio.capital.utils.formatMoney
import com.yistudio.data.entity.TaskEntity
import com.yistudio.data.entity.TaskStatus
import com.yistudio.data.entity.TaskType
import com.yistudio.capital.R

@Composable
fun TaskCard(
    task: TaskEntity,
    onClaim: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Title and Category Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Dark
                    )
                    
                    val icon = when (task.type) {
                        TaskType.SPRINT -> R.drawable.baseline_task_24
                        TaskType.MILESTONE -> R.drawable.baseline_task_24
                        TaskType.MOONSHOT -> R.drawable.baseline_task_24
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painterResource(R.drawable.baseline_task_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = task.type.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                RewardBadge(task)
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            // Body: Technical Description
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Section
            val progress = (task.currentProgress.toFloat() / task.targetGoal).coerceIn(0f, 1f)
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(CircleShape),
                    color = Dark,
                    trackColor = Color(0xFFEEEEEE)
                )
                
                Spacer(Modifier.width(12.dp))
                
                Text(
                    text = "${task.currentProgress}/${task.targetGoal}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Dark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button
            val isClaimable = task.status == TaskStatus.COMPLETED
            val isClaimed = task.status == TaskStatus.CLAIMED
            
            Button(
                onClick = { onClaim(task.id) },
                enabled = isClaimable,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isClaimable) Dark else Color(0xFFF5F5F5),
                    contentColor = if (isClaimable) Color.White else Color.Gray,
                    disabledContainerColor = if (isClaimed) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                    disabledContentColor = if (isClaimed) Color(0xFF4CAF50) else Color.Gray
                ),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(
                    text = when (task.status) {
                        TaskStatus.LOCKED -> "LOCKED"
                        TaskStatus.ACTIVE -> "IN PROGRESS"
                        TaskStatus.COMPLETED -> "CLAIM REWARD"
                        TaskStatus.CLAIMED -> "COMPLETED"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RewardBadge(task: TaskEntity) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (task.rewardInfluence > 0) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⭐", fontSize = 12.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = formatMoney(task.rewardInfluence.toDouble()).removePrefix("$"),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
        
        if (task.rewardEquity > 0) {
            if (task.rewardInfluence > 0) Spacer(Modifier.width(8.dp))
            Surface(
                color = Dark,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Golden)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "📈", fontSize = 12.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${task.rewardEquity}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Golden
                    )
                }
            }
        }
    }
}
