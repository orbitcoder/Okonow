package com.noitacilppa.okonow.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant

@Composable
fun TodayProgressSection(progress: Float, tasksCompletedToday: Int, hasTasks: Boolean = true) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TodayProgressRing(progress = progress, hasTasks = hasTasks)
        
        if (hasTasks) {
            Text(
                text = buildAnnotatedString {
                    append("You've completed ")
                    withStyle(SpanStyle(color = OnSurface, fontWeight = FontWeight.Bold)) {
                        append("$tasksCompletedToday tasks")
                    }
                    append(" today.")
                },
                color = OnSurfaceVariant,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            Text(
                text = "Your schedule is clear.",
                color = OnSurfaceVariant,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TodayProgressSectionPreview() {
    TodayProgressSection(progress = 0.4f, tasksCompletedToday = 2)
}
