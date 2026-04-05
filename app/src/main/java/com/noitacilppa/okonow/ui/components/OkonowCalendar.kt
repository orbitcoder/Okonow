package com.noitacilppa.okonow.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.*
import dev.chrisbanes.haze.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun OkonowCalendar(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    var viewMonth by remember { mutableStateOf(YearMonth.from(selectedDate ?: LocalDate.now())) }
    var currentSelectedDate by remember { mutableStateOf(selectedDate ?: LocalDate.now()) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 1. Scrim with Blur
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeEffect(state = hazeState) {
                    backgroundColor = Background
                    blurRadius = 24.dp
                    tints = listOf(HazeTint(Color.Black.copy(alpha = 0.5f)))
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest
                )
        )

        // 2. Calendar Content
        val today = remember { LocalDate.now() }
        val maxDate = remember { today.plusMonths(12) }
        val canGoBack = viewMonth.isAfter(YearMonth.from(today))
        val canGoForward = viewMonth.isBefore(YearMonth.from(maxDate))

        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f)
                        )
                    )
                )
                .hazeEffect(state = hazeState) {
                    backgroundColor = Background
                    blurRadius = 40.dp
                    tints = listOf(HazeTint(Background.copy(alpha = 0.2f)))
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header: Month/Year navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewMonth = viewMonth.minusMonths(1) },
                        enabled = canGoBack
                    ) {
                        Icon(
                            Icons.Default.ChevronLeft, 
                            contentDescription = "Previous Month", 
                            tint = if (canGoBack) OnSurface else OnSurfaceVariant.copy(alpha = 0.3f)
                        )
                    }
                    Text(
                        text = "${viewMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${viewMonth.year}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { viewMonth = viewMonth.plusMonths(1) },
                        enabled = canGoForward
                    ) {
                        Icon(
                            Icons.Default.ChevronRight, 
                            contentDescription = "Next Month", 
                            tint = if (canGoForward) OnSurface else OnSurfaceVariant.copy(alpha = 0.3f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Days of week
                Row(modifier = Modifier.fillMaxWidth()) {
                    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    daysOfWeek.forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Grid of days
                val firstDayOfMonth = viewMonth.atDay(1).dayOfWeek.value // 1 (Mon) to 7 (Sun)
                val daysInMonth = viewMonth.lengthOfMonth()
                val totalSlots = 42 // 6 weeks
                
                val dates = mutableListOf<LocalDate?>()
                // Padding for the first week
                repeat(firstDayOfMonth - 1) { dates.add(null) }
                // Current month dates
                for (day in 1..daysInMonth) {
                    dates.add(viewMonth.atDay(day))
                }
                // Padding for the rest
                while (dates.size < totalSlots) { dates.add(null) }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    userScrollEnabled = false
                ) {
                    items(dates) { date ->
                        val isSelectable = date != null && !date.isBefore(today) && !date.isBefore(maxDate.minusMonths(12)) && !date.isAfter(maxDate)
                        val isSelected = date != null && date == currentSelectedDate

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .then(
                                    if (isSelected) {
                                        Modifier.background(
                                            Brush.linearGradient(
                                                listOf(PrimaryPurple, TertiaryPink)
                                            )
                                        )
                                    } else Modifier
                                )
                                .clickable(enabled = isSelectable) {
                                    if (date != null) {
                                        currentSelectedDate = date
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (date != null) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    color = when {
                                        isSelected -> Background
                                        !isSelectable -> OnSurface.copy(alpha = 0.2f)
                                        else -> OnSurface
                                    },
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("CANCEL", color = OnSurfaceVariant, letterSpacing = 1.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    val isSelectionValid = !currentSelectedDate.isBefore(today) && !currentSelectedDate.isAfter(maxDate)

                    Button(
                        onClick = { 
                            onDateSelected(currentSelectedDate)
                            onDismissRequest()
                        },
                        enabled = isSelectionValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple,
                            contentColor = Background,
                            disabledContainerColor = SurfaceVariant.copy(alpha = 0.5f),
                            disabledContentColor = OnSurface.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("DONE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
