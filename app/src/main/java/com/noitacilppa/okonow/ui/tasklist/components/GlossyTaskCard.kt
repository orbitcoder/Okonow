package com.noitacilppa.okonow.ui.tasklist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.data.Priority
import com.noitacilppa.okonow.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

private data class PriorityColors(
    val background: Color,
    val text: Color,
    val border: Color
)

@Composable
private fun getPriorityColors(priority: Priority): PriorityColors {
    return when (priority) {
        Priority.LOW -> PriorityColors(
            background = SecondaryTeal.copy(alpha = 0.1f),
            text = SecondaryTeal,
            border = SecondaryTeal.copy(alpha = 0.2f)
        )
        Priority.MEDIUM -> PriorityColors(
            background = PrimaryPurple.copy(alpha = 0.1f),
            text = PrimaryPurple,
            border = PrimaryPurple.copy(alpha = 0.2f)
        )
        Priority.HIGH -> PriorityColors(
            background = TertiaryPink.copy(alpha = 0.1f),
            text = TertiaryPink,
            border = TertiaryPink.copy(alpha = 0.2f)
        )
        Priority.URGENT -> PriorityColors(
            background = Color(0xFFFF5252).copy(alpha = 0.1f),
            text = Color(0xFFFF5252),
            border = Color(0xFFFF5252).copy(alpha = 0.2f)
        )
        Priority.CRITICAL -> PriorityColors(
            background = Color(0xFFD32F2F).copy(alpha = 0.15f),
            text = Color(0xFFD32F2F),
            border = Color(0xFFD32F2F).copy(alpha = 0.3f)
        )
    }
}

@Composable
fun GlossyTaskCard(
    title: String,
    subtitle: String,
    priority: Priority,
    subtasksDone: Int,
    subtasksTotal: Int,
    category: String,
    categoryColor: Color,
    checkboxAccent: Color,
    shape: Shape,
    reminderTime: Date? = null,
    taskDate: Date? = null,
    initialChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val priorityColors = getPriorityColors(priority)
    // Keep internal state for immediate UI feedback if desired, 
    // but usually in a production app with a database, it's better to rely on the passed-in state.
    // However, to satisfy "reflect in ring progress", we MUST call onCheckedChange.
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SurfaceContainerHigh,
                        SurfaceContainerHigh.copy(alpha = 0.8f)
                    )
                )
            )
            .border(1.dp, OutlineVariant.copy(alpha = 0.15f), shape)
            .clickable { onCheckedChange(!initialChecked) }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Interactive Checkbox
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (initialChecked) checkboxAccent else Color.Transparent)
                    .border(
                        2.dp,
                        if (initialChecked) checkboxAccent else OnSurfaceVariant.copy(alpha = 0.3f),
                        RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (initialChecked) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Background
                    )
                }
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        title,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = OnSurface
                    )

                    /*Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(99.dp))
                            .background(priorityColors.background)
                            .border(1.dp, priorityColors.border, RoundedCornerShape(99.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            priority.name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = priorityColors.text
                        )
                    }*/
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

                    if (subtitle.isNotBlank()) {
                        Text(
                            subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                    }
                    if (reminderTime != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Alarm,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = OnSurfaceVariant
                            )
                            val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
                            Text(
                                "schedule ${formatter.format(reminderTime)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant
                            )
                        }
                    } else if (taskDate != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Event,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = OnSurfaceVariant
                            )
                            val formatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
                            Text(
                                formatter.format(taskDate),
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }

                if (subtasksTotal > 0) {
                    val progressValue = subtasksDone.toFloat() / subtasksTotal
                    val barColor = when {
                        progressValue < 0.5f -> TertiaryPink
                        progressValue < 1f -> PrimaryPurple
                        else -> SecondaryTeal
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { progressValue },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(CircleShape),
                            color = barColor,
                            trackColor = SurfaceContainerHighest
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "$subtasksDone/$subtasksTotal",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurfaceVariant
                        )
                    }
                }

                Box(
                       modifier = Modifier
                           .clip(RoundedCornerShape(99.dp))
                           .background(priorityColors.background)
                           .defaultMinSize(minWidth = 50.dp)
                           .border(1.dp, priorityColors.border, RoundedCornerShape(99.dp))
                           .padding(horizontal = 10.dp, vertical = 2.dp)
                   ) {
                       Text(
                           category,
                           fontSize = 10.sp,
                           fontWeight = FontWeight.Bold,
                           color = priorityColors.text,
                           style = TextStyle(
                               platformStyle = PlatformTextStyle(
                                   includeFontPadding = false
                               )
                           )
                       )
                   }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun GlossyTaskCardPreview() {
    var checked by remember { mutableStateOf(false) }
    GlossyTaskCard(
        title = "Morning Run djsdsjndnjsdsjnnjdsjndsnjds dsjndsjdnsjndsj",
        subtitle = "5km in the park",
        priority = Priority.HIGH,
        subtasksDone = 1,
        subtasksTotal = 1,
        category = "a",
        categoryColor = TertiaryPink,
        checkboxAccent = PrimaryPurple,
        shape = RoundedCornerShape(24.dp),
        reminderTime = null,
        taskDate = Date(),
        initialChecked = checked,
        onCheckedChange = { checked = it }
    )
}
