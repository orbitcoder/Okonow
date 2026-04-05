package com.noitacilppa.okonow.ui.tasklist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.data.TaskDetailed
import com.noitacilppa.okonow.ui.TodoViewModel
import com.noitacilppa.okonow.ui.tasklist.components.GlossyTaskCard
import com.noitacilppa.okonow.ui.theme.*

enum class TaskListTab(val label: String) {
    UPCOMING("Upcoming"),
    COMPLETED("Completed")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FullTaskListScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: TodoViewModel,
    onTaskClick: (TaskDetailed) -> Unit,
    initialTab: TaskListTab = TaskListTab.UPCOMING
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(initialTab) }
    
    val filteredTasks = remember(uiState.tasks, selectedTab) {
        when (selectedTab) {
            TaskListTab.UPCOMING -> uiState.tasks.filter { !it.task.isCompleted }
            TaskListTab.COMPLETED -> uiState.tasks.filter { it.task.isCompleted }
        }
    }
    
    val groupedTasks = groupTasksByDate(filteredTasks)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Background,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Tasks",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = OnSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Background
                    )
                )
                
                // Custom Tab Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerHigh.copy(alpha = 0.5f))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TaskListTab.entries.forEach { tab ->
                        val isSelected = selectedTab == tab
                        val backgroundColor by animateColorAsState(
                            targetValue = if (isSelected) PrimaryPurple else Color.Transparent,
                            animationSpec = spring(stiffness = Spring.StiffnessLow),
                            label = "tabBackground"
                        )
                        val textColor by animateColorAsState(
                            targetValue = if (isSelected) Background else OnSurfaceVariant,
                            label = "tabText"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(backgroundColor)
                                .clickable { selectedTab = tab },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = textColor,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            groupedTasks.forEach { (groupName, groupList) ->
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Background.copy(alpha = 0.9f))
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = groupName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }
                }

                items(groupList, key = { it.task.id }) { taskItem ->
                    val task = taskItem.task
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
                        onCheckedChange = { viewModel.toggleTaskDone(task) },
                        onClick = { onTaskClick(taskItem) }
                    )
                }
            }
            
            if (groupedTasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (selectedTab == TaskListTab.UPCOMING) 
                                "All caught up! No upcoming tasks." 
                            else "No completed tasks yet.",
                            color = OnSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
