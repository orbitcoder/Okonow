package com.noitacilppa.okonow.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.*

@Composable
fun EmptyTasksState(onAddTask: () -> Unit, onToggleDemo: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(HomeCardCorner))
            .background(SurfaceContainerHigh)
            .border(1.dp, OutlineVariant.copy(alpha = 0.15f), RoundedCornerShape(HomeCardCorner))
            .padding(vertical = 48.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Decorative background glow
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(SecondaryTeal.copy(alpha = 0.1f), CircleShape)
                .blur(40.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(PrimaryPurple.copy(alpha = 0.1f))
                    .border(2.dp, PrimaryPurple.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "All done",
                    tint = PrimaryFixedDim,
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Clear Skies Ahead",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "You have no tasks remaining for today. Relax, or get ahead on tomorrow's goals.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAddTask,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple,
                    contentColor = Color(0xFF330066)
                ),
                shape = RoundedCornerShape(99.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add New Task", fontWeight = FontWeight.Bold)
            }

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun EmptyTasksStatePreview() {
    Box(Modifier.padding(16.dp)) {
        EmptyTasksState(onAddTask = {}, onToggleDemo = {})
    }
}
