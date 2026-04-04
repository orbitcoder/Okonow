package com.noitacilppa.okonow.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.*

@Composable
fun QuoteMotivationCard(shape: Shape) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(SecondaryTeal.copy(alpha = 0.1f), TertiaryPink.copy(alpha = 0.05f))
                )
            )
            .border(1.dp, OutlineVariant.copy(alpha = 0.1f), shape)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.FormatQuote,
                contentDescription = null,
                tint = SecondaryTeal,
                modifier = Modifier.size(32.dp)
            )
            Text(
                "Editorial precision is not just about the details; it's about the space between them.",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                color = OnSurface,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
            Text(
                "— THE CURATOR",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = SecondaryTeal
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun QuoteMotivationCardPreview() {
    Box(Modifier.padding(16.dp)) {
        QuoteMotivationCard(shape = RoundedCornerShape(24.dp))
    }
}
