package com.noitacilppa.okonow.ui.home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlin.math.*
import kotlin.random.Random
import com.noitacilppa.okonow.ui.theme.*

@Composable
fun TodayProgressRing(progress: Float, hasTasks: Boolean = true) {
    var confettiKey by remember { mutableIntStateOf(0) }
    LaunchedEffect(progress) {
        if (progress >= 1f && hasTasks) {
            confettiKey++
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.85f),
        label = "progress_anim"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(240.dp)) {
        val handleCenterState = remember { mutableStateOf<Offset?>(null) }
        val handleRadius = 8.dp
        val density = LocalDensity.current
        val handleRadiusPx = with(density) { handleRadius.toPx() }

        val progressBrush = remember {
            Brush.sweepGradient(
                colors = listOf(
                    SecondaryTeal,
                    PrimaryPurple,
                    TertiaryPink,
                    SecondaryTeal
                )
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()
            val center = Offset(size.width / 2, size.height / 2)
            val radius = (size.minDimension - strokeWidth - (handleRadiusPx * 2)) / 2

            // Background dynamic glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SecondaryTeal.copy(alpha = 0.15f * pulseAlpha), Color.Transparent),
                    center = center,
                    radius = radius * 1.25f
                ),
                radius = radius * 1.25f,
                center = center
            )

            // Outline Track
            drawArc(
                color = SurfaceContainerHighest,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth)
            )

            if (animatedProgress > 0f) {
                val sweep = animatedProgress * 360f

                // Radiating Glow
                drawArc(
                    brush = progressBrush,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth * 1.8f, cap = StrokeCap.Round),
                    alpha = pulseAlpha * 0.6f
                )

                // Main Core Progress Arc
                drawArc(
                    brush = progressBrush,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // The handle/knob at the end of progress
                val a = Math.toRadians(-90.0 + sweep.toDouble())
                val hx = center.x + cos(a).toFloat() * radius
                val hy = center.y + sin(a).toFloat() * radius
                val handleCenter = Offset(hx, hy)
                handleCenterState.value = handleCenter
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (hasTasks) {
                Text(
                    "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = OnSurface
                )
                Text(
                    "COMPLETED",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = SecondaryTeal
                )
            } else {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = SecondaryTeal
                )
                Text(
                    "ALL DONE",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = SecondaryTeal,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        ConfettiOverlay(
            key = confettiKey,
            enabled = hasTasks && progress >= 1f,
            modifier = Modifier.matchParentSize()
        )

    }
}

@Composable
private fun ConfettiOverlay(
    key: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    if (!enabled || key == 0) return

    // particles tied to key so each celebration regenerates
    val particles = remember(key) { generateParticles(count = 100) }
    val t = remember(key) { Animatable(0f) }

    LaunchedEffect(key) {
        t.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = 4000,
                easing = LinearOutSlowInEasing
            )
        )
    }

    val alpha = (1f - t.value).coerceIn(0f, 1f)

    Canvas(modifier = modifier) {
        // === EXPLICIT CENTER CALCULATION (fixes the right-shift) ===
        val centerX = size.width / 2f
        val centerY = size.height / 2f

        val strokeWidth = 14.dp.toPx()
        val handleRadiusPx = 8.dp.toPx()

        // Same exact radius math as the main progress ring
        val radius = (minOf(size.width, size.height) - strokeWidth - (handleRadiusPx * 2)) / 2f

        // Burst point exactly at the TOP of the progress circle
        // (vertically perfect, horizontally now forced to true center)
        val originOffset = Offset(
            x = centerX - radius,                    // ← this was drifting right before
            y = centerY - radius
        )

        val time = t.value

        // Pixel scales
        val maxDistancePx = size.minDimension * 0.45f
        val gravityPx = size.minDimension * 0.6f

        particles.forEach { p ->
            val dx = p.vx * maxDistancePx * time
            val dy = p.vy * maxDistancePx * time + (time * time * gravityPx)

            val x = originOffset.x + dx
            val y = originOffset.y + dy

            val rot = p.rot + (time * p.rotSpeed)
            val sizePx = lerp(4f, 12f, p.sizeBias)

            val color = p.color.copy(alpha = alpha)

            withTransform({
                translate(left = x, top = y)
                rotate(degrees = rot)
            }) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(-sizePx / 2f, -sizePx / 2f),
                    size = Size(sizePx, sizePx * 0.7f),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
            }
        }
    }
}

private data class Particle(
    val vx: Float,
    val vy: Float,
    val rot: Float,
    val rotSpeed: Float,
    val sizeBias: Float,
    val color: Color
)

private fun generateParticles(count: Int): List<Particle> {
    val palette = listOf(
        SecondaryTeal,
        PrimaryPurple,
        TertiaryPink,
        Color(0xFFFFD37C),
        Color(0xFF6CE7FF)
    )
    return List(count) {
        val angle = Random.nextFloat() * 2 * PI.toFloat()
        val speed = Random.nextFloat() * 1.5f + 0.5f
        Particle(
            vx = cos(angle) * speed,
            vy = sin(angle) * speed - 0.4f, // Upward bias
            rot = Random.nextFloat() * 360f,
            rotSpeed = (Random.nextFloat() - 0.5f) * 720f,
            sizeBias = Random.nextFloat(),
            color = palette.random()
        )
    }
}

@Preview(name = "Today Progress Ring - Partial")
@Composable
fun TodayProgressRingPartialPreview() {
    OkonowTheme {
        Surface {
            TodayProgressRing(progress = 0.65f)
        }
    }
}

@Preview(name = "Today Progress Ring - Completed")
@Composable
fun TodayProgressRingCompletedPreview() {
    OkonowTheme {
        Surface {
            TodayProgressRing(progress = 1f)
        }
    }
}

@Preview(name = "Today Progress Ring - No Tasks")
@Composable
fun TodayProgressRingNoTasksPreview() {
    OkonowTheme {
        Surface {
            TodayProgressRing(progress = 0f, hasTasks = false)
        }
    }
}
