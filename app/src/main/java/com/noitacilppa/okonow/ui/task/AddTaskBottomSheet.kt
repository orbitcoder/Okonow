package com.noitacilppa.okonow.ui.task

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.noitacilppa.okonow.ui.task.components.TaskTitleInput
import com.noitacilppa.okonow.ui.theme.Background
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SecondaryTeal
import com.noitacilppa.okonow.ui.theme.SurfaceContainer
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import kotlinx.coroutines.delay
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

    LaunchedEffect(Unit) {
        delay(80)
        titleFocusRequester.requestFocus()
        keyboard?.show()
    }

    // Root container
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Blur the entire background content from MainShell
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeEffect(state = hazeState) {
                    backgroundColor = Background
                    blurRadius = 20.dp
                    tints = listOf(HazeTint(Color.Black.copy(alpha = 0.4f)))
                }
        )

        // 2. Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .navigationBarsPadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Container for the decorative glows and the sheet surface
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetHeight)
            ) {
                // 3. Decorative Background Glows
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

                // 4. The glass sheet surface
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
                                onDismiss = onDismiss
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
                                        // Switching from Description to Subtasks
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
                                        // Switching from Subtasks to Description
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

                            // Detail Pills
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

                            // AI Suggestions
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
                                    onDismiss()
                                },
                                onDelete = onDismiss
                            )
                        }
                    }
                }
            }
        }
        
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDateMillis ?: System.currentTimeMillis()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        selectedDateMillis = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }

    BackHandler(onBack = onDismiss)
}
