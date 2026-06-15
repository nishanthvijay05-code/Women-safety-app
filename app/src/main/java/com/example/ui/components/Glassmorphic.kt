package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GuardPink
import com.example.ui.theme.SecurePurple

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    isDark: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val bgColor = if (isDark) {
        Color(0x501E1E28) // 31% opacity slate dark
    } else {
        Color(0x60FFFFFF) // 37% opacity white
    }

    val borderColor = if (isDark) {
        Color(0x12FFFFFF) // Sleek border border-white/5
    } else {
        Color(0x226366F1) // 13% opacity Indigo
    }

    Column(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(cornerRadius),
                clip = false,
                ambientColor = Color.Black.copy(alpha = 0.5f),
                spotColor = Color.Black.copy(alpha = 0.5f)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(bgColor, bgColor.copy(alpha = 0.05f))
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor,
                        borderColor.copy(alpha = 0.05f),
                        borderColor.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .padding(16.dp),
        content = content
    )
}

@Composable
fun GlowBackground(
    modifier: Modifier = Modifier,
    isDark: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    // Elegant background animation
    val infiniteTransition = rememberInfiniteTransition(label = "glow_bg")
    val animOffset1 by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )
    val animOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )

    val bgBrush = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF0D0D12), Color(0xFF070709))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xFFFAF9FC), Color(0xFFEBE9F0))
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .drawBehind {
                if (isDark) {
                    // Draw glowing ambient spots
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(GuardPink.copy(alpha = 0.08f), Color.Transparent)
                        ),
                        radius = 450f,
                        center = center.copy(
                            x = center.x + animOffset1,
                            y = center.y - 300f
                        )
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(SecurePurple.copy(alpha = 0.08f), Color.Transparent)
                        ),
                        radius = 500f,
                        center = center.copy(
                            x = center.x - animOffset2,
                            y = center.y + 400f
                        )
                    )
                } else {
                    // Soft light ambient reflections
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(GuardPink.copy(alpha = 0.05f), Color.Transparent)
                        ),
                        radius = 400f,
                        center = center.copy(
                            x = center.x + animOffset2,
                            y = center.y - 200f
                        )
                    )
                }
            },
        content = content
    )
}

@Composable
fun PulsingSOSRing(
    modifier: Modifier = Modifier,
    color: Color = GuardPink,
    targetScale: Float = 1.6f,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale1 by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = targetScale,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale1"
    )
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha1"
    )

    val scale2 by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = targetScale - 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale2"
    )
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha2"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        // Outer wave 1
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        color = color.copy(alpha = alpha1),
                        radius = (size.minDimension / 2f) * scale1
                    )
                }
        )
        // Outer wave 2
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        color = color.copy(alpha = alpha2),
                        radius = (size.minDimension / 2f) * scale2
                    )
                }
        )
        // Core button content
        content()
    }
}
