package com.noitacilppa.okonow.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.*

@Composable
fun GlossyTaskCard(
    title: String,
    subtitle: String,
    priorityLabel: String,
    priorityBackground: Color,
    priorityText: Color,
    priorityBorder: Color,
    subtasksDone: Int,
    subtasksTotal: Int,
    category: String,
    categoryColor: Color,
    checkboxAccent: Color,
    shape: Shape,
    initialChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    // Keep internal state for immediate UI feedback if desired, 
    // but usually in a production app with a database, it's better to rely on the passed-in state.
    // However, to satisfy "reflect in ring progress", we MUST call onCheckedChange.
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceContainerHigh)
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
                        category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = categoryColor
                    )
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(99.dp))
                            .background(priorityBackground)
                            .border(1.dp, priorityBorder, RoundedCornerShape(99.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            priorityLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = priorityText
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { if (subtasksTotal > 0) subtasksDone.toFloat() / subtasksTotal else 0f },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape),
                        color = checkboxAccent,
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
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun GlossyTaskCardPreview() {
    var checked by remember { mutableStateOf(false) }
    GlossyTaskCard(
        title = "Morning Run",
        subtitle = "5km in the park",
        priorityLabel = "High",
        priorityBackground = PrimaryPurple.copy(alpha = 0.1f),
        priorityText = PrimaryFixedDim,
        priorityBorder = PrimaryPurple.copy(alpha = 0.2f),
        subtasksDone = 1,
        subtasksTotal = 1,
        category = "Fitness",
        categoryColor = TertiaryPink,
        checkboxAccent = PrimaryPurple,
        shape = RoundedCornerShape(24.dp),
        initialChecked = checked,
        onCheckedChange = { checked = it }
    )
}
