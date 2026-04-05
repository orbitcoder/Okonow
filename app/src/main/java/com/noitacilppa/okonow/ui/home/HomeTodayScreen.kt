package com.noitacilppa.okonow.ui.home

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
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
import com.noitacilppa.okonow.ui.tasklist.TaskListTab
import com.noitacilppa.okonow.ui.tasklist.components.GlossyTaskCard
import com.noitacilppa.okonow.ui.tasklist.groupTasksByDate
import com.noitacilppa.okonow.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

/** Stitch Home / Today — header avatar. */
private const val HomeHeaderAvatarUrl =
    "https://lh3.googleusercontent.com/aida-public/AB6AXuBX2iavuLwaAuBEShstIw4wW3UfA5FwCFXmdCe2q-pg_F3nyallzVK6gkG_iYYfMwAeNlYpHxctHGPqm_qp49-Q1BHHxS8B166fi11QGKsPLW3Y92P6uci6W7FqKybhdoN9vAtNooxwRbt6BFQbK2BwbSn3MxsL_wzg0QrH57s0n4zKnFkjCq_PYGOz5oOQl7Zb9klTVcyOyXlguCNQNwH8IQ7CV5vOT_PPeEQRSRDQKjEoUYYmKvaRwlefMX1deprVMt6VcLXE8NA"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTodayScreen(
    modifier: Modifier = Modifier,
    onSettings: () -> Unit = {},
    onSeeAllTasks: (TaskListTab) -> Unit = {},
    onAddTask: () -> Unit = {},
    viewModel: TodoViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }
    val userName by userPreferences.userName.collectAsState(initial = "Alex")
    
    val uiState by viewModel.uiState.collectAsState()
    val allTasks = uiState.tasks
    
    // Maintain a list of tasks that were recently completed to keep them visible for a bit
    val recentlyCompletedIds = remember { mutableStateListOf<Long>() }
    // Maintain a list of tasks that are currently animating out
    val exitingIds = remember { mutableStateListOf<Long>() }
    
    val focusedTasks = remember(allTasks, recentlyCompletedIds.toList(), exitingIds.toList()) {
        allTasks.filter { 
            !it.task.isCompleted || 
            recentlyCompletedIds.contains(it.task.id) || 
            exitingIds.contains(it.task.id) 
        }
    }
    
    val hasTasks = allTasks.isNotEmpty()

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    val tasksCompletedToday = allTasks.count { it.task.isCompleted }
    val totalTasksToday = allTasks.size
    val progress = if (allTasks.isEmpty()) 1.0f else tasksCompletedToday.toFloat() / totalTasksToday

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
                    totalTasksToday = totalTasksToday,
                    hasTasks = hasTasks
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (focusedTasks.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                "Focused Tasks",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = OnSurface
                            )
                            Text(
                                "See all",
                                style = MaterialTheme.typography.labelLarge,
                                color = PrimaryPurple,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onSeeAllTasks(TaskListTab.UPCOMING) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        val groupedTasks = groupTasksByDate(focusedTasks)
                        var count = 0

                        groupedTasks.forEach { (groupName, groupList) ->
                            if (count < 6) {
                                Text(
                                    groupName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                )

                                val remainingSlots = 6 - count
                                val itemsToShow = groupList.take(remainingSlots)

                                itemsToShow.forEach { taskItem ->
                                    val task = taskItem.task
                                    val isVisible = !exitingIds.contains(task.id)
                                    
                                    LaunchedEffect(isVisible) {
                                        if (!isVisible) {
                                            delay(500) // Match animation duration
                                            exitingIds.remove(task.id)
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = isVisible,
                                        enter = fadeIn(),
                                        exit = slideOutHorizontally(
                                            targetOffsetX = { -it },
                                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                                        ) + fadeOut(),
                                        label = "TaskVisibility"
                                    ) {
                                        GlossyTaskCard(
                                            title = task.title,
                                            subtitle = task.details,
                                            priority = task.priority,
                                            subtasksDone = taskItem.subtasks.count { it.isCompleted },
                                            subtasksTotal = taskItem.subtasks.size,
                                            category = taskItem.tags.firstOrNull()?.name ?: "Focus",
                                            categoryColor = SecondaryTeal,
                                            checkboxAccent = PrimaryPurple,
                                            shape = RoundedCornerShape(24.dp),
                                            reminderTime = task.reminderTime,
                                            taskDate = task.endTime,
                                            initialChecked = task.isCompleted,
                                            onCheckedChange = { isChecked ->
                                                viewModel.toggleTaskDone(task)
                                                if (isChecked) {
                                                    recentlyCompletedIds.add(task.id)
                                                    scope.launch {
                                                        delay(3000) // 3-second buffer for undoing
                                                        if (recentlyCompletedIds.contains(task.id)) {
                                                            exitingIds.add(task.id)
                                                            recentlyCompletedIds.remove(task.id)
                                                        }
                                                    }
                                                } else {
                                                    recentlyCompletedIds.remove(task.id)
                                                    exitingIds.remove(task.id)
                                                }
                                            }
                                        )
                                    }
                                }
                                count += itemsToShow.size
                            }
                        }
                    } else if (!hasTasks) {
                        EmptyTasksState(onAddTask = onAddTask, onToggleDemo = {
                            Toast.makeText(context, "Demo toggle coming soon", Toast.LENGTH_SHORT).show()
                        })
                    } else {
                        // All tasks completed but has tasks
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                "All focused tasks completed! 🎉",
                                style = MaterialTheme.typography.bodyLarge,
                                color = OnSurfaceVariant
                            )
                            
                            Button(
                                onClick = { onSeeAllTasks(TaskListTab.COMPLETED) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryPurple.copy(alpha = 0.1f),
                                    contentColor = PrimaryPurple
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "View Completed Tasks",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Icon(
                                        Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
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
                    .padding(end = 24.dp, bottom = 25.dp),
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
