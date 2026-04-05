package com.noitacilppa.okonow.ui.focus

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noitacilppa.okonow.ui.AppViewModelProvider
import com.noitacilppa.okonow.ui.TodoViewModel
import com.noitacilppa.okonow.ui.focus.components.ActiveTaskCard
import com.noitacilppa.okonow.ui.focus.components.FocusCalendar
import com.noitacilppa.okonow.ui.focus.components.FocusTimer
import com.noitacilppa.okonow.ui.theme.*
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun FocusScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    
    val activeTask = remember(uiState.tasks, uiState.activeTaskId) {
        uiState.tasks.find { it.task.id == uiState.activeTaskId }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Decorative background blooms
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .size(300.dp)
                .background(PrimaryPurple.copy(alpha = 0.1f), shape = CircleShape)
                .blur(100.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-50).dp, y = 100.dp)
                .size(250.dp)
                .background(SecondaryTeal.copy(alpha = 0.05f), shape = CircleShape)
                .blur(80.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, bottom = 120.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            item {
                FocusTimer()
            }

            if (activeTask != null) {
                item {
                    ActiveTaskCard(
                        task = activeTask,
                        onComplete = {
                            viewModel.toggleTaskDone(activeTask.task)
                            viewModel.setActiveTask(null)
                        }
                    )
                }
            }
            
            item {
                FocusCalendar(
                    tasks = uiState.tasks,
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    activeTaskId = uiState.activeTaskId,
                    onTaskSelected = { task ->
                        viewModel.setActiveTask(task.task.id)
                    }
                )
            }
        }
    }
}


