package com.noitacilppa.okonow.ui.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.Background
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.OutlineVariant
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SurfaceContainerHighest

private val PrimaryDim = Color(0xFF9547F7)
private val OnPrimaryContainer = Color(0xFF330066)

@Composable
fun TaskActionButtons(
    onSave: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, Background.copy(alpha = 0.95f))
                )
            )
            .padding(horizontal = 0.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Brush.linearGradient(listOf(PrimaryPurple, PrimaryDim)))
                    .clickable { onSave() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = OnPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "Save Task",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnPrimaryContainer
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(SurfaceContainerHighest)
                    .border(1.dp, OutlineVariant.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = OnSurfaceVariant)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TaskActionButtonsPreview() {
    TaskActionButtons(onSave = {}, onDelete = {})
}
