package com.noitacilppa.okonow.ui.focus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.data.TaskDetailed
import com.noitacilppa.okonow.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import java.time.ZoneId

@Composable
fun FocusCalendar(
    tasks: List<TaskDetailed>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onTaskSelected: (TaskDetailed) -> Unit,
    activeTaskId: Long?,
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
    
    val filteredTasks = remember(tasks, selectedDate) {
        tasks.filter { taskItem ->
            val taskDate = taskItem.task.endTime?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
            taskDate == selectedDate && !taskItem.task.isCompleted
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OnSurface
            )
            Row {
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Prev", tint = OnSurfaceVariant)
                }
                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next", tint = OnSurfaceVariant)
                }
            }
        }

        // Calendar Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainerLow)
                .border(1.dp, OutlineVariant, RoundedCornerShape(16.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Days labels
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
            
            // Grid
            CalendarGrid(
                yearMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = onDateSelected,
                tasks = tasks
            )
        }

        // Tasks List for selected date
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "TASKS FOR ${selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd"))}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceVariant,
                letterSpacing = 1.sp
            )
            
            if (filteredTasks.isEmpty()) {
                Text(
                    text = "No tasks planned for this day.",
                    fontSize = 14.sp,
                    color = OnSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                filteredTasks.forEach { task ->
                    val isActive = task.task.id == activeTaskId
                    TaskSelectionItem(
                        task = task,
                        isActive = isActive,
                        onClick = { onTaskSelected(task) }
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    tasks: List<TaskDetailed>
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    
    // Day of week of first day (1-7, Mon-Sun)
    val move = firstDayOfMonth.dayOfWeek.value - 1
    
    val days = mutableListOf<LocalDate?>()
    for (i in 0 until move) days.add(null)
    for (i in 1..yearMonth.lengthOfMonth()) days.add(yearMonth.atDay(i))
    
    val rows = days.chunked(7)
    
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { date ->
                    if (date != null) {
                        val hasTask = tasks.any {
                            it.task.endTime?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate() == date
                        }
                        FocusCalendarDay(
                            day = date.dayOfMonth.toString(),
                            isCurrent = date == selectedDate,
                            highlightTask = hasTask,
                            modifier = Modifier.weight(1f),
                            onClick = { onDateSelected(date) }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                // Fill if row has less than 7 items
                if (row.size < 7) {
                    repeat(7 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun FocusCalendarDay(
    day: String,
    modifier: Modifier = Modifier,
    isCurrent: Boolean = false,
    highlightTask: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    isCurrent -> SecondaryTeal
                    highlightTask -> PrimaryPurple.copy(alpha = 0.2f)
                    else -> SurfaceContainerHighest
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            fontSize = 12.sp,
            fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
            color = if (isCurrent) Background else OnSurface
        )
        if (highlightTask && !isCurrent) {
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

@Composable
fun TaskSelectionItem(
    task: TaskDetailed,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isActive) SecondaryTeal.copy(alpha = 0.15f) else SurfaceContainerHigh)
            .border(
                1.dp,
                if (isActive) SecondaryTeal else OutlineVariant,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isActive) SecondaryTeal else OnSurfaceVariant.copy(alpha = 0.3f))
        )
        Text(
            text = task.task.title,
            fontSize = 14.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) SecondaryTeal else OnSurface,
            modifier = Modifier.weight(1f)
        )
        if (isActive) {
            Text(
                "ACTIVE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = SecondaryTeal,
                letterSpacing = 1.sp
            )
        }
    }
}

@Preview
@Composable
fun FocusCalendarPreview() {
    // FocusCalendar(...)
}
