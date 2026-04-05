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
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
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
import com.noitacilppa.okonow.ui.theme.PrimaryPurple

@Composable
fun SuggestionChip(
    icon: @Composable () -> Unit,
    label: String,
    border: Color,
    background: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(background)
            .border(1.dp, border, RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}

@Preview
@Composable
fun SuggestionChipPreview() {
    SuggestionChip(
        icon = { Icon(Icons.Outlined.AutoAwesome, null, tint = PrimaryPurple, modifier = Modifier.size(16.dp)) },
        label = "Remind me 10 mins before",
        border = PrimaryPurple.copy(alpha = 0.1f),
        background = PrimaryPurple.copy(alpha = 0.05f),
        textColor = PrimaryPurple
    )
}
