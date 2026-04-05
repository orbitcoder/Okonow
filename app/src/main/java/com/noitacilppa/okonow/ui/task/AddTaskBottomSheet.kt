package com.noitacilppa.okonow.ui.task

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.noitacilppa.okonow.ui.task.components.BottomSheetHandle
import com.noitacilppa.okonow.ui.task.components.DetailPill
import com.noitacilppa.okonow.ui.task.components.SuggestionChip
import com.noitacilppa.okonow.ui.task.components.TaskActionButtons
import com.noitacilppa.okonow.ui.task.components.TaskDescriptionInput
import com.noitacilppa.okonow.ui.task.components.TaskHeader
import com.noitacilppa.okonow.ui.components.OkonowCalendar
import com.noitacilppa.okonow.ui.task.components.TaskTitleInput
import com.noitacilppa.okonow.ui.theme.Background
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SecondaryTeal
import com.noitacilppa.okonow.ui.theme.SurfaceContainer
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.HazeTint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val SheetTopRadius = 24.dp

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String, String, List<String>, String?) -> Unit,
    hazeState: HazeState
) {
    // 1. Logic & State
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var attachmentUri by remember { mutableStateOf<Uri?>(null) }
    var isSubtaskMode by remember { mutableStateOf(false) }
    val subtasks = remember { mutableStateListOf<String>() }
    
    var selectedDateMillis by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    val titleFocusRequester = remember { FocusRequester() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val sheetHeight = screenHeight * 0.8f
    val keyboard = LocalSoftwareKeyboardController.current

    // 2. Animation Lifecycle
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    val transition = updateTransition(targetState = isVisible, label = "SheetTransition")
    
    // Scrim and Blur animations
    val scrimAlpha by transition.animateFloat(label = "ScrimAlpha") { if (it) 0.45f else 0f }
    val backgroundBlur by transition.animateDp(label = "BgBlur") { if (it) 20.dp else 0.dp }

    // Helper to dismiss with exit animation
    val scope = rememberCoroutineScope()
    val dismissWithAnimation = {
        isVisible = false
        scope.launch {
            delay(300) // Match exit animation duration
            onDismiss()
        }
    }

    BackHandler(enabled = isVisible, onBack = { dismissWithAnimation() })

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(150) // Wait for slide to settle slightly
            titleFocusRequester.requestFocus()
            keyboard?.show()
        }
    }

    // Root container
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Blur and Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeEffect(state = hazeState) {
                    backgroundColor = Background
                    blurRadius = backgroundBlur
                    tints = listOf(HazeTint(Color.Black.copy(alpha = scrimAlpha * 0.8f)))
                }
                .background(Color.Black.copy(alpha = scrimAlpha))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { dismissWithAnimation() }
                )
        )

        // 2. Sheet Content
        transition.AnimatedVisibility(
            visible = { it },
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow)
            ) + fadeIn(tween(300)),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + fadeOut(tween(250))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetHeight)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Background Glows (Stationary relative to sheet)
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 40.dp, y = 96.dp)
                            .size(256.dp)
                            .hazeSource(state = hazeState)
                            .background(PrimaryPurple.copy(alpha = 0.15f), CircleShape)
                            .blur(80.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = (-40).dp, y = (-200).dp)
                            .size(256.dp)
                            .hazeSource(state = hazeState)
                            .background(SecondaryTeal.copy(alpha = 0.12f), CircleShape)
                            .blur(80.dp)
                    )
                }

                // Glass sheet surface
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = SheetTopRadius, topEnd = SheetTopRadius))
                        .hazeEffect(state = hazeState) {
                            backgroundColor = Background
                            blurRadius = 30.dp
                            tints = listOf(HazeTint(Background.copy(alpha = 0.7f)))
                        }
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    SurfaceContainer.copy(alpha = 0.2f),
                                    Background.copy(alpha = 0.5f)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        BottomSheetHandle()

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .padding(top = 8.dp, bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            TaskHeader(
                                title = "New Task",
                                onDismiss = { dismissWithAnimation() }
                            )

                            TaskTitleInput(
                                value = title,
                                onValueChange = { title = it },
                                focusRequester = titleFocusRequester
                            )

                            TaskDescriptionInput(
                                value = description,
                                onValueChange = { description = it },
                                onAttachmentChange = { attachmentUri = it },
                                attachmentUri = attachmentUri,
                                isSubtaskMode = isSubtaskMode,
                                subtasks = subtasks,
                                onSubtaskChange = { index, newValue -> subtasks[index] = newValue },
                                onAddSubtask = { subtasks.add("") },
                                onToggleMode = { 
                                    if (!isSubtaskMode) {
                                        val plainText = description.trim()
                                        if (plainText.isNotEmpty()) {
                                            if (subtasks.isNotEmpty() && subtasks[0].isBlank()) {
                                                subtasks[0] = plainText
                                            } else {
                                                subtasks.add(0, plainText)
                                            }
                                            description = ""
                                        }
                                    } else {
                                        val combined = subtasks.filter { it.isNotBlank() }.joinToString("\n")
                                        if (combined.isNotEmpty()) {
                                            description = combined
                                            subtasks.clear()
                                        }
                                    }
                                    isSubtaskMode = !isSubtaskMode 
                                    if (isSubtaskMode && subtasks.isEmpty()) {
                                        subtasks.add("")
                                    }
                                }
                            )

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val dateText = remember(selectedDateMillis) {
                                    selectedDateMillis?.let {
                                        val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
                                        sdf.format(Date(it))
                                    } ?: "Today"
                                }
                                
                                DetailPill(
                                    icon = { Icon(Icons.Default.CalendarMonth, null, tint = PrimaryPurple, modifier = Modifier.size(20.dp)) },
                                    text = dateText,
                                    onClick = { showDatePicker = true }
                                )
                                DetailPill(
                                    icon = { Icon(Icons.Default.PriorityHigh, null, tint = PrimaryPurple, modifier = Modifier.size(20.dp)) },
                                    text = "Priority"
                                )
                                DetailPill(
                                    icon = { Icon(Icons.Default.Label, null, tint = PrimaryPurple, modifier = Modifier.size(20.dp)) },
                                    text = "Inbox"
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                SuggestionChip(
                                    icon = { Icon(Icons.Outlined.AutoAwesome, null, tint = PrimaryPurple, modifier = Modifier.size(16.dp)) },
                                    label = "Break into 3 sub-tasks",
                                    border = PrimaryPurple.copy(alpha = 0.1f),
                                    background = PrimaryPurple.copy(alpha = 0.05f),
                                    textColor = PrimaryPurple,
                                    onClick = {}
                                )
                                SuggestionChip(
                                    icon = { Icon(Icons.Default.Event, null, tint = SecondaryTeal, modifier = Modifier.size(16.dp)) },
                                    label = "Schedule for tomorrow morning",
                                    border = SecondaryTeal.copy(alpha = 0.1f),
                                    background = SecondaryTeal.copy(alpha = 0.05f),
                                    textColor = SecondaryTeal,
                                    onClick = {}
                                )
                            }

                            TaskActionButtons(
                                onSave = {
                                    onSave(title, description, subtasks.toList(), attachmentUri?.toString())
                                    dismissWithAnimation()
                                },
                                onDelete = { dismissWithAnimation() }
                            )
                        }
                    }
                }
            }
        }
        
        if (showDatePicker) {
            val initialDate = remember(selectedDateMillis) {
                selectedDateMillis?.let {
                    java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                } ?: java.time.LocalDate.now()
            }
            
            OkonowCalendar(
                selectedDate = initialDate,
                onDateSelected = { localDate ->
                    selectedDateMillis = localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                },
                onDismissRequest = { showDatePicker = false },
                hazeState = hazeState
            )
        }
    }
}
