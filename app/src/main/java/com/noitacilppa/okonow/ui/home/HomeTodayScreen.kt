package com.noitacilppa.okonow.ui.home

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.noitacilppa.okonow.data.UserPreferences
import com.noitacilppa.okonow.ui.TodoViewModel
import com.noitacilppa.okonow.ui.home.components.*
import com.noitacilppa.okonow.ui.theme.*
import java.util.Calendar

/** Stitch Home / Today — header avatar. */
private const val HomeHeaderAvatarUrl =
    "https://lh3.googleusercontent.com/aida-public/AB6AXuBX2iavuLwaAuBEShstIw4wW3UfA5FwCFXmdCe2q-pg_F3nyallzVK6gkG_iYYfMwAeNlYpHxctHGPqm_qp49-Q1BHHxS8B166fi11QGKsPLW3Y92P6uci6W7FqKybhdoN9vAtNooxwRbt6BFQbK2BwbSn3MxsL_wzg0QrH57s0n4zKnFkjCq_PYGOz5oOQl7Zb9klTVcyOyXlguCNQNwH8IQ7CV5vOT_PPeEQRSRDQKjEoUYYmKvaRwlefMX1deprVMt6VcLXE8NA"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTodayScreen(
    modifier: Modifier = Modifier,
    onSettings: () -> Unit = {},
    onSeeAllTasks: () -> Unit = {},
    onAddTask: () -> Unit = {},
    viewModel: TodoViewModel
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userName by userPreferences.userName.collectAsState(initial = "Alex")
    val cardShape = RoundedCornerShape(HomeCardCorner)
    
    val uiState by viewModel.uiState.collectAsState()
    val tasks = uiState.tasks
    val hasTasks = tasks.isNotEmpty()

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    val tasksCompletedToday = tasks.count { it.task.isCompleted }
    val progress = if (tasks.isEmpty()) 1.0f else tasksCompletedToday.toFloat() / tasks.size

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(2.dp, PrimaryPurple.copy(alpha = 0.2f), CircleShape)
                                .padding(2.dp)
                                .clip(CircleShape)
                        ) {
                            AsyncImage(
                                model = HomeHeaderAvatarUrl,
                                contentDescription = "User profile",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column {
                            Text(
                                "Okonow",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Text(
                                "$greeting, ${userName ?: "Alex"}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = OnSurfaceVariant,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                },
                actions = {
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background.copy(alpha = 0.92f)
                )
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 112.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                TodayProgressSection(
                    progress = progress,
                    tasksCompletedToday = tasksCompletedToday,
                    hasTasks = hasTasks
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (hasTasks) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                "Today's Focus",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Text(
                                "See all",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple,
                                modifier = Modifier.clickable {
                                    onSeeAllTasks()
                                    Toast.makeText(context, "Full task list coming soon", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }

                    if (hasTasks) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            tasks.forEach { taskDetailed ->
                                val task = taskDetailed.task
                                GlossyTaskCard(
                                    title = task.title,
                                    subtitle = task.details,
                                    priorityLabel = task.priority.name,
                                    priorityBackground = PrimaryPurple.copy(alpha = 0.1f),
                                    priorityText = PrimaryFixedDim,
                                    priorityBorder = PrimaryPurple.copy(alpha = 0.2f),
                                    subtasksDone = taskDetailed.subtasks.count { it.isCompleted },
                                    subtasksTotal = taskDetailed.subtasks.size.coerceAtLeast(1),
                                    category = "Task",
                                    categoryColor = TertiaryPink,
                                    checkboxAccent = PrimaryPurple,
                                    shape = cardShape,
                                    initialChecked = task.isCompleted,
                                    onCheckedChange = { viewModel.toggleTaskDone(task) }
                                )
                            }
                        }
                    } else {
                        EmptyTasksState(
                            onAddTask = onAddTask,
                            onToggleDemo = { /* Demo toggle removed as we have real data now */ }
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        "Team Activity",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(modifier = Modifier.width(0.dp))
                        teamMembers.forEach { member ->
                            TeamAvatarChip(member = member)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                QuoteMotivationCard(shape = RoundedCornerShape(HomeCardCorner))
            }
        }

        if (hasTasks) {
            FloatingActionButton(
                onClick = onAddTask,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 96.dp),
                containerColor = PrimaryPurple,
                contentColor = Color(0xFF330066),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 8.dp
                ),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add task", modifier = Modifier.size(28.dp))
            }
        }
    }
}
