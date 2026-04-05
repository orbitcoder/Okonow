package com.noitacilppa.okonow.ui.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noitacilppa.okonow.data.TaskDetailed
import com.noitacilppa.okonow.ui.AppViewModelProvider
import com.noitacilppa.okonow.ui.TodoViewModel
import com.noitacilppa.okonow.ui.home.HomeTodayScreen
import com.noitacilppa.okonow.ui.profile.ProfileScreen
import com.noitacilppa.okonow.ui.task.AddTaskBottomSheet
import com.noitacilppa.okonow.ui.theme.Background
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.focus.FocusScreen
import com.noitacilppa.okonow.ui.tasklist.FullTaskListScreen
import com.noitacilppa.okonow.ui.tasklist.TaskListTab
import androidx.activity.compose.BackHandler
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

private enum class MainTab(
    val saveKey: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    TASKS("tasks", "Tasks", Icons.Default.Checklist),
    FOCUS("focus", "Focus", Icons.Default.Timer),
    PROFILE("profile", "Profile", Icons.Default.Person)
}

@Composable
fun MainShell(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.TASKS.saveKey) }
    var showAddTask by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<TaskDetailed?>(null) }
    var showFullTaskList by remember { mutableStateOf(false) }
    var initialTaskListTab by remember { mutableStateOf(TaskListTab.UPCOMING) }
    val hazeState = remember { HazeState() }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (!showAddTask && editingTask == null && !showFullTaskList) {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)),
                    color = Background.copy(alpha = 0.88f),
                    tonalElevation = 8.dp
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp
                    ) {
                        MainTab.entries.forEach { tab ->
                            val selected = tab.saveKey == selectedTab
                            NavigationBarItem(
                                selected = selected,
                                onClick = { 
                                    selectedTab = tab.saveKey 
                                    showFullTaskList = false
                                },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.label,
                                        modifier = Modifier
                                    )
                                },
                                label = {
                                    Text(
                                        tab.label.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 1.sp
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PrimaryPurple,
                                    selectedTextColor = PrimaryPurple,
                                    unselectedIconColor = OnSurface.copy(alpha = 0.4f),
                                    unselectedTextColor = OnSurface.copy(alpha = 0.4f),
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Content to be captured for blur
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(state = hazeState)
            ) {
                // Background inside the source
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background)
                )

                if (showFullTaskList) {
                    BackHandler { showFullTaskList = false }
                    FullTaskListScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBack = { showFullTaskList = false },
                        viewModel = viewModel,
                        onTaskClick = { editingTask = it },
                        initialTab = initialTaskListTab
                    )
                } else {
                    when (selectedTab) {
                        MainTab.TASKS.saveKey -> HomeTodayScreen(
                            modifier = Modifier.fillMaxSize(),
                            onSettings = { selectedTab = MainTab.PROFILE.saveKey },
                            onSeeAllTasks = { tab ->
                                initialTaskListTab = tab
                                showFullTaskList = true
                            },
                            onAddTask = { showAddTask = true },
                            onTaskClick = { editingTask = it },
                            viewModel = viewModel
                        )
                        MainTab.FOCUS.saveKey -> FocusScreen(Modifier.fillMaxSize())
                        MainTab.PROFILE.saveKey -> ProfileScreen(
                            modifier = Modifier.fillMaxSize(),
                            todoViewModel = viewModel
                        )
                    }
                }
            }

            if (showAddTask || editingTask != null) {
                AddTaskBottomSheet(
                    onDismiss = { 
                        showAddTask = false
                        editingTask = null
                    },
                    initialTask = editingTask,
                    onSave = { title, description, subtasks, attachmentUri, tag, endTime, reminderTime ->
                        if (editingTask != null) {
                            viewModel.updateTask(
                                taskId = editingTask!!.task.id,
                                title = title,
                                description = description,
                                subtasks = subtasks,
                                attachmentUri = attachmentUri,
                                tag = tag,
                                endTime = endTime,
                                reminderTime = reminderTime
                            )
                        } else {
                            viewModel.addTask(title, description, subtasks, attachmentUri, tag, endTime, reminderTime)
                        }
                        
                        Toast.makeText(
                            context,
                            if (editingTask != null) "Task updated" else "Task saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    hazeState = hazeState
                )
            }
        }
    }
}

@Composable
private fun TabPlaceholder(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            color = OnSurfaceVariant
        )
    }
}
