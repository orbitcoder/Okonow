package com.noitacilppa.okonow.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.PrimaryPurple

@Composable
fun TodayProgressSection(
    progress: Float,
    tasksCompletedToday: Int,
    totalTasksToday: Int,
    hasTasks: Boolean = true
) {
    val tasksLeft = totalTasksToday - tasksCompletedToday

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TodayProgressRing(progress = progress, hasTasks = hasTasks)
        
        if (hasTasks) {
            val message = buildAnnotatedString {
                if (tasksLeft > 0) {
                    append("You have ")
                    withStyle(SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.ExtraBold)) {
                        append("$tasksLeft ${if (tasksLeft == 1) "task" else "tasks"}")
                    }
                    append(" left in your bucket.")
                } else {
                    withStyle(SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.ExtraBold)) {
                        append("Yeah! ")
                    }
                    append("You've completed all your goals for today. Keep that momentum going!")
                }
            }

            Text(
                text = message,
                color = OnSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 32.dp)
            )
            
            if (tasksLeft > 0) {
                val motivationalQuotes = listOf(
                    "Stay focused, you're doing great!",
                    "Each task is a step towards your vision.",
                    "Small progress is still progress. Keep going!",
                    "You've got this! One focus session at a time."
                )
                // Using tasksLeft as a seed to pick a quote so it's consistent for that state
                val quote = motivationalQuotes[tasksLeft % motivationalQuotes.size]
                
                Text(
                    text = quote,
                    color = OnSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.padding(1.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E10)
@Composable
fun TodayProgressSectionPreview() {
    TodayProgressSection(progress = 0.6f, tasksCompletedToday = 3, totalTasksToday = 5)
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E10)
@Composable
fun TodayProgressSectionDonePreview() {
    TodayProgressSection(progress = 1f, tasksCompletedToday = 5, totalTasksToday = 5)
}
