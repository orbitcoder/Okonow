package com.noitacilppa.okonow.ui.focus.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun FocusTimer(
    modifier: Modifier = Modifier
) {
    var totalSeconds by remember { mutableIntStateOf(30 * 60) }
    var maxSeconds by remember { mutableIntStateOf(30 * 60) }
    var isActive by remember { mutableStateOf(false) }

    LaunchedEffect(isActive) {
        while (isActive && totalSeconds > 0) {
            delay(1000)
            totalSeconds--
            if (totalSeconds == 0) {
                isActive = false
            }
        }
    }

    val progress = if (maxSeconds > 0) totalSeconds.toFloat() / maxSeconds.toFloat() else 0f
    val sweepAngle = progress * 360f

    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // Circular Progress Track
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = SurfaceContainerHighest,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
                
                // Focus Ring
                val gradient = Brush.sweepGradient(
                    colors = listOf(Color.Transparent, SecondaryTeal, Color.Transparent)
                )
                drawArc(
                    brush = gradient,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeString,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    color = OnSurface,
                    letterSpacing = (-2).sp
                )
                Text(
                    text = "DEEP FOCUS SESSION",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryTeal,
                    letterSpacing = 4.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            // Interaction Handles
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add 30 mins
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHighest)
                        .border(1.dp, OutlineVariant, CircleShape)
                        .clickable { 
                            if (maxSeconds + 1800 <= 90 * 60) {
                                totalSeconds += 1800
                                maxSeconds += 1800
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add 30m", tint = OnSurface)
                }

                // Play/Pause
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SecondaryTeal)
                        .clickable { 
                            if (totalSeconds == 0) {
                                totalSeconds = 30 * 60
                                maxSeconds = 30 * 60
                                isActive = true
                            } else {
                                isActive = !isActive
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isActive) Icons.Default.Pause else Icons.Default.PlayArrow, 
                        contentDescription = if (isActive) "Pause" else "Play", 
                        tint = Background,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Finish
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHighest)
                        .border(1.dp, OutlineVariant, CircleShape)
                        .clickable { 
                            totalSeconds = 0
                            isActive = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Finish", tint = OnSurface)
                }
            }
        }
    }
}

@Preview
@Composable
fun FocusTimerPreview() {
    FocusTimer()
}
