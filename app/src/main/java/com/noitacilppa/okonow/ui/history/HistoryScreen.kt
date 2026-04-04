package com.noitacilppa.okonow.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.*

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(Background)) {
        // Decorative shimmer/blooms
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-50).dp)
                .size(400.dp, 100.dp)
                .background(PrimaryPurple.copy(alpha = 0.15f), shape = CircleShape)
                .blur(80.dp)
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 48.dp, bottom = 120.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item {
                Text(
                    text = "History",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = OnSurface,
                    letterSpacing = (-1).sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Review your past achievements.",
                    fontSize = 14.sp,
                    color = OnSurfaceVariant
                )
            }
            
            item {
                HistorySection(
                    title = "YESTERDAY",
                    tasks = listOf(
                        HistoryTask("Morning Standup", "Sync"),
                        HistoryTask("Update dependencies", "Config"),
                        HistoryTask("Design System Review", "Design")
                    )
                )
            }
            
            item {
                HistorySection(
                    title = "LAST WEEK",
                    tasks = listOf(
                        HistoryTask("Release 0.9.0", "Milestone"),
                        HistoryTask("Refactor Navigation", "Architecture")
                    )
                )
            }
        }
    }
}

@Composable
fun HistorySection(title: String, tasks: List<HistoryTask>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryPurple.copy(alpha = 0.8f),
            letterSpacing = 3.sp,
            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
        )
        
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            tasks.forEach { task ->
                HistoryTaskCard(task)
            }
        }
    }
}

@Composable
fun HistoryTaskCard(task: HistoryTask) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLow.copy(alpha = 0.7f))
            .border(1.dp, PrimaryPurple.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Complete circle indicator with shimmer
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(PrimaryPurple),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                modifier = Modifier.size(14.dp),
                tint = Background
            )
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface.copy(alpha = 0.5f),
                textDecoration = TextDecoration.LineThrough,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = task.tag.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceVariant,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

data class HistoryTask(val title: String, val tag: String)
