package com.noitacilppa.okonow.ui.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OutlineVariant
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SurfaceContainerHighest

@Composable
fun DetailPill(
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(SurfaceContainerHighest.copy(alpha = 0.5f))
            .border(1.dp, OutlineVariant.copy(alpha = 0.12f), RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = OnSurface)
    }
}

@Preview
@Composable
fun DetailPillPreview() {
    DetailPill(
        icon = { Icon(Icons.Default.Event, null, tint = PrimaryPurple, modifier = Modifier.size(22.dp)) },
        text = "Tomorrow, 3:00 PM"
    )
}
