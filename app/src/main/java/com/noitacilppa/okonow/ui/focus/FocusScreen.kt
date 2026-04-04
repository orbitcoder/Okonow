package com.noitacilppa.okonow.ui.focus

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.*

@Composable
fun FocusScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Decorative background blooms
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .size(300.dp)
                .background(PrimaryPurple.copy(alpha = 0.1f), shape = CircleShape)
                .blur(100.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-50).dp, y = 100.dp)
                .size(250.dp)
                .background(SecondaryTeal.copy(alpha = 0.05f), shape = CircleShape)
                .blur(80.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, bottom = 120.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            item {
                FocusTimerSection()
            }
            item {
                CalendarSection()
            }
        }
    }
}

@Composable
fun FocusTimerSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(280.dp),
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
                    sweepAngle = 250f, // active portion
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "24:59",
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
            
            // Interaction Handles overlayed at bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHighest)
                        .border(1.dp, OutlineVariant, CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = OnSurface)
                }

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SecondaryTeal)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause, 
                        contentDescription = "Pause", 
                        tint = Background,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHighest)
                        .border(1.dp, OutlineVariant, CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.SkipNext, contentDescription = "Skip Next", tint = OnSurface)
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Current Task Bubble
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainerLow)
                .border(1.dp, OutlineVariant, RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(SecondaryTeal)
            )
            Column {
                Text(
                    text = "ACTIVE TASK",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceVariant,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Design System Refinement",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
            }
        }
    }
}

@Composable
fun CalendarSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Calendar",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OnSurface
            )
            Row {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Prev", tint = OnSurfaceVariant)
                }
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next", tint = OnSurfaceVariant)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainerLow)
                .border(1.dp, OutlineVariant, RoundedCornerShape(16.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").forEach {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Grid simulation
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Week 1
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CalendarDay("28", enabled = false, modifier = Modifier.weight(1f))
                    CalendarDay("29", enabled = false, modifier = Modifier.weight(1f))
                    CalendarDay("30", enabled = false, modifier = Modifier.weight(1f))
                    CalendarDay("31", enabled = false, modifier = Modifier.weight(1f))
                    CalendarDay("1", modifier = Modifier.weight(1f))
                    CalendarDay("2", modifier = Modifier.weight(1f))
                    CalendarDay("3", modifier = Modifier.weight(1f))
                }
                // Week 2
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CalendarDay("4", modifier = Modifier.weight(1f))
                    CalendarDay("5", modifier = Modifier.weight(1f))
                    CalendarDay("6", modifier = Modifier.weight(1f))
                    CalendarDay("7", highlightTask = true, modifier = Modifier.weight(1f))
                    CalendarDay("8", modifier = Modifier.weight(1f))
                    CalendarDay("9", modifier = Modifier.weight(1f))
                    CalendarDay("10", modifier = Modifier.weight(1f))
                }
                // Week 3
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CalendarDay("11", isCurrent = true, modifier = Modifier.weight(1f))
                    CalendarDay("12", modifier = Modifier.weight(1f))
                    CalendarDay("13", modifier = Modifier.weight(1f))
                    CalendarDay("14", modifier = Modifier.weight(1f))
                    CalendarDay("15", modifier = Modifier.weight(1f))
                    CalendarDay("16", modifier = Modifier.weight(1f))
                    CalendarDay("17", modifier = Modifier.weight(1f))
                }
            }
        }

        // Up Next Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainerHigh)
                .border(1.dp, OutlineVariant, RoundedCornerShape(16.dp))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(TertiaryPink.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Upcoming", tint = TertiaryPink)
            }
            Column {
                Text(
                    text = "PRIORITY TOMORROW",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceVariant,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Release Candidate v1.0",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
                Text(
                    text = "Tomorrow, 10:00 AM",
                    fontSize = 12.sp,
                    color = OnSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CalendarDay(
    day: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isCurrent: Boolean = false,
    highlightTask: Boolean = false
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    isCurrent -> SecondaryTeal
                    highlightTask -> PrimaryPurple.copy(alpha = 0.2f)
                    enabled -> SurfaceContainerHighest
                    else -> Color.Transparent
                }
            )
            .then(
                if (highlightTask && !isCurrent) Modifier.border(1.dp, PrimaryPurple.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                else Modifier
            )
            .then(
                if (enabled) Modifier.clickable { } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            fontSize = 12.sp,
            fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
            color = when {
                isCurrent -> Background
                highlightTask -> PrimaryPurple
                enabled -> OnSurface
                else -> OnSurface.copy(alpha = 0.4f)
            }
        )
        if (highlightTask) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(PrimaryPurple)
            )
        }
    }
}
