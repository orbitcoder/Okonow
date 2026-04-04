package com.noitacilppa.okonow.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.SurfaceContainerHigh
import com.noitacilppa.okonow.ui.theme.TertiaryPink

@Composable
fun TeamAvatarChip(member: TeamMember) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh)
                .border(2.dp, member.borderColor, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
        ) {
            AsyncImage(
                model = member.imageUrl,
                contentDescription = member.name,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        if (member.dimmed) alpha = 0.4f
                    },
                contentScale = ContentScale.Crop
            )
        }
        Text(
            member.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (member.dimmed) OnSurfaceVariant.copy(alpha = 0.5f) else OnSurfaceVariant
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TeamAvatarChipPreview() {
    TeamAvatarChip(
        member = TeamMember(
            name = "Marcus",
            imageUrl = "",
            borderColor = TertiaryPink
        )
    )
}
