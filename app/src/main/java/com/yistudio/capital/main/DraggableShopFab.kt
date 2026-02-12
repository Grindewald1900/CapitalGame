package com.yistudio.capital.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yistudio.capital.R
import com.yistudio.capital.ui.theme.Golden
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DraggableShopFab(
    maxWidthPx: Float,
    maxHeightPx: Float,
    onFabClick: () -> Unit
) {
    val density = LocalDensity.current
    val fabWidth = 48.dp
    val fabHeight = 64.dp
    val fabWidthPx = with(density) { fabWidth.toPx() }
    val fabHeightPx = with(density) { fabHeight.toPx() }

    // 关键：安全边距，防止手指落在系统返回手势判定区
    val safeMargin = 8.dp
    val safeMarginPx = with(density) { safeMargin.toPx() }
    val verticalPaddingPx = with(density) { 32.dp.toPx() }

    val offsetX = remember { Animatable(maxWidthPx - fabWidthPx - safeMarginPx) }
    val offsetY = remember { Animatable(maxHeightPx * 0.7f) }

    var isDragging by remember { mutableStateOf(false) }
    var isPeeking by remember { mutableStateOf(true) }
    val isAtLeft = offsetX.value < maxWidthPx / 2

    // 动态书签形状：靠近边缘的一侧为直角，另一侧为圆角
    val bookmarkShape = if (isAtLeft) {
        RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
    } else {
        RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
    }

    val scope = rememberCoroutineScope()
    var autoHideJob by remember { mutableStateOf<Job?>(null) }

    fun startAutoHideTimer() {
        autoHideJob?.cancel()
        autoHideJob = scope.launch {
            delay(3000)
            if (!isDragging) {
                val targetPeekX = if (isAtLeft) {
                    -fabWidthPx * 0.4f // 向左隐藏
                } else {
                    maxWidthPx - fabWidthPx * 0.6f // 向右隐藏
                }
                offsetX.animateTo(targetPeekX, spring(stiffness = Spring.StiffnessLow))
                isPeeking = true
            }
        }
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .size(width = fabWidth, height = fabHeight)
            // 解决误触核心 1：排除系统手势
            .systemGestureExclusion()
            .shadow(
                elevation = if (isDragging) 10.dp else 2.dp,
                shape = bookmarkShape,
                spotColor = Golden
            )
            .alpha(if (isDragging || !isPeeking) 1.0f else 0.6f)
            .clip(bookmarkShape)
            .background(Color(0xFF53668E))
            .border(1.dp, Golden.copy(alpha = 0.5f), bookmarkShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (isPeeking) {
                            scope.launch {
                                // 解决误触核心 2：展开时也保留 safeMargin，不让手指贴边
                                val targetX = if (isAtLeft) safeMarginPx else maxWidthPx - fabWidthPx - safeMarginPx
                                offsetX.animateTo(targetX)
                                isPeeking = false
                                startAutoHideTimer()
                            }
                        } else {
                            onFabClick()
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                        isPeeking = false
                        autoHideJob?.cancel()
                    },
                    onDragEnd = {
                        isDragging = false
                        // 吸附时保持 safeMargin
                        val targetX = if (offsetX.value + fabWidthPx / 2 < maxWidthPx / 2) {
                            safeMarginPx
                        } else {
                            maxWidthPx - fabWidthPx - safeMarginPx
                        }
                        scope.launch {
                            offsetX.animateTo(targetX, spring(dampingRatio = Spring.DampingRatioLowBouncy))
                            startAutoHideTimer()
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            // 允许拖动到边缘，但 end 时会弹回 safeMargin
                            val newX = (offsetX.value + dragAmount.x).coerceIn(-fabWidthPx, maxWidthPx)
                            val newY = (offsetY.value + dragAmount.y).coerceIn(
                                verticalPaddingPx,
                                maxHeightPx - fabHeightPx - verticalPaddingPx
                            )
                            offsetX.snapTo(newX)
                            offsetY.snapTo(newY)
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // 书签上的金色装饰线，增强质感
        Box(
            modifier = Modifier
                .align(if (isAtLeft) Alignment.CenterStart else Alignment.CenterEnd)
                .width(4.dp)
                .fillMaxHeight(0.6f)
                .background(Golden, RoundedCornerShape(2.dp))
        )

        Icon(
            painter = painterResource(id = R.drawable.outline_add_shopping_cart_24),
            contentDescription = "Equity Desk",
            tint = Golden,
            modifier = Modifier.size(24.dp)
        )
    }
}