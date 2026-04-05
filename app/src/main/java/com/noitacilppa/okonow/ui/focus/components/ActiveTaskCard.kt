package com.noitacilppa.okonow.ui.focus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.data.TaskDetailed
import com.noitacilppa.okonow.ui.theme.*

@Composable
fun ActiveTaskCard(
    task: TaskDetailed,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLow)
            .border(1.dp, OutlineVariant, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Complete Checkbox (Tick)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .border(1.dp, SecondaryTeal.copy(alpha = 0.5f), CircleShape)
                .clickable { onComplete() },
            contentAlignment = Alignment.Center
        ) {
            // We'll show a teal dot or empty circle, and on click mark it
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(SecondaryTeal)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "ACTIVE TASK",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceVariant,
                letterSpacing = 2.sp
            )
            Text(
                text = task.task.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
        }

        // Completion indicator
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Complete",
            tint = OnSurfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier
                .size(20.dp)
                .clickable { onComplete() }
        )
    }
}

@Preview
@Composable
fun ActiveTaskCardPreview() {
    // Mock task for preview
    // Note: TaskDetailed requires real entities, so this might need a mock factory if complex.
    // For now, I'll just skip the complex preview or use a simpler shell.
}
