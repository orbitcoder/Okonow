package com.noitacilppa.okonow.ui.task

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.noitacilppa.okonow.ui.task.components.BottomSheetHandle
import com.noitacilppa.okonow.ui.task.components.DetailPill
import com.noitacilppa.okonow.ui.task.components.SuggestionChip
import com.noitacilppa.okonow.ui.task.components.TaskActionButtons
import com.noitacilppa.okonow.ui.task.components.TaskDescriptionInput
import com.noitacilppa.okonow.ui.task.components.TaskHeader
import com.noitacilppa.okonow.ui.components.OkonowCalendar
import com.noitacilppa.okonow.ui.task.components.TaskTitleInput
import com.noitacilppa.okonow.ui.theme.Background
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SecondaryTeal
import com.noitacilppa.okonow.ui.theme.SurfaceContainer
import com.noitacilppa.okonow.ui.theme.SurfaceVariant
import com.noitacilppa.okonow.ui.theme.TertiaryPink
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.HazeTint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val SheetTopRadius = 24.dp

data class SubtaskState(
    val description: String,
    val isDone: Boolean = false
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String, String, List<SubtaskState>, String?, String, Date?, Date?) -> Unit,
    hazeState: HazeState
) {
    // 1. Logic & State
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var attachmentUri by remember { mutableStateOf<Uri?>(null) }
    var isSubtaskMode by remember { mutableStateOf(false) }
    val subtasks = remember { mutableStateListOf<SubtaskState>() }
    
    var selectedDateMillis by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    var selectedTag by remember { mutableStateOf("Inbox") }
    var showTagPicker by remember { mutableStateOf(false) }

    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    val titleFocusRequester = remember { FocusRequester() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val sheetHeight = screenHeight * 0.8f
    val keyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, check for exact alarm permission
            checkAndRequestExactAlarmPermission(context) {
                showTimePicker = true
            }
        }
    }

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
                                subtasks = subtasks.map { it.description },
                                subtaskStates = subtasks,
                                onSubtaskChange = { index, newValue -> 
                                    subtasks[index] = subtasks[index].copy(description = newValue) 
                                },
                                onSubtaskToggle = { index ->
                                    subtasks[index] = subtasks[index].copy(isDone = !subtasks[index].isDone)
                                },
                                onAddSubtask = { subtasks.add(SubtaskState("")) },
                                onToggleMode = { 
                                    if (!isSubtaskMode) {
                                        val plainText = description.trim()
                                        if (plainText.isNotEmpty()) {
                                            if (subtasks.isNotEmpty() && subtasks[0].description.isBlank()) {
                                                subtasks[0] = subtasks[0].copy(description = plainText)
                                            } else {
                                                subtasks.add(0, SubtaskState(plainText))
                                            }
                                            description = ""
                                        }
                                    } else {
                                        val combined = subtasks.filter { it.description.isNotBlank() }.joinToString("\n") { it.description }
                                        if (combined.isNotEmpty()) {
                                            description = combined
                                            subtasks.clear()
                                        }
                                    }
                                    isSubtaskMode = !isSubtaskMode 
                                    if (isSubtaskMode && subtasks.isEmpty()) {
                                        subtasks.add(SubtaskState(""))
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
                                    icon = { Icon(Icons.Default.Label, null, tint = PrimaryPurple, modifier = Modifier.size(20.dp)) },
                                    text = selectedTag,
                                    onClick = { showTagPicker = true }
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                val reminderText = if (selectedTime != null) {
                                    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
                                    val dateFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())
                                    val dateString = selectedDateMillis?.let { dateFormatter.format(Date(it)) } ?: "Today"
                                    "You will be notified at ${selectedTime!!.format(timeFormatter)} on $dateString"
                                } else {
                                    "Schedule reminder notification for the task"
                                }

                                SuggestionChip(
                                    icon = { Icon(Icons.Default.Notifications, null, tint = SecondaryTeal, modifier = Modifier.size(16.dp)) },
                                    label = reminderText,
                                    border = SecondaryTeal.copy(alpha = 0.1f),
                                    background = SecondaryTeal.copy(alpha = 0.05f),
                                    textColor = SecondaryTeal,
                                    onClick = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            if (ContextCompat.checkSelfPermission(
                                                    context,
                                                    Manifest.permission.POST_NOTIFICATIONS
                                                ) != PackageManager.PERMISSION_GRANTED
                                            ) {
                                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                            } else {
                                                checkAndRequestExactAlarmPermission(context) {
                                                    showTimePicker = true
                                                }
                                            }
                                        } else {
                                            checkAndRequestExactAlarmPermission(context) {
                                                showTimePicker = true
                                            }
                                        }
                                    }
                                )
                            }

                            TaskActionButtons(
                                onSave = {
                                    val finalEndTime = selectedDateMillis?.let { Date(it) }
                                    val finalReminderTime = if (selectedTime != null && selectedDateMillis != null) {
                                        val cal = Calendar.getInstance()
                                        cal.timeInMillis = selectedDateMillis!!
                                        cal.set(Calendar.HOUR_OF_DAY, selectedTime!!.hour)
                                        cal.set(Calendar.MINUTE, selectedTime!!.minute)
                                        cal.set(Calendar.SECOND, 0)
                                        cal.time
                                    } else null

                                    onSave(title, description, subtasks.toList(), attachmentUri?.toString(), selectedTag, finalEndTime, finalReminderTime)
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

        if (showTagPicker) {
            OkonowPickerDialog(
                title = "Select Tag",
                options = listOf("Work", "Personal", "Shopping", "Health", "Inbox"),
                selectedOption = selectedTag,
                onOptionSelected = { selectedTag = it },
                onDismissRequest = { showTagPicker = false },
                hazeState = hazeState
            )
        }

        if (showTimePicker) {
            OkonowTimePicker(
                onTimeSelected = { selectedTime = it },
                onDismissRequest = { showTimePicker = false },
                hazeState = hazeState
            )
        }
    }
}

private fun checkAndRequestExactAlarmPermission(context: Context, onGranted: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        } else {
            onGranted()
        }
    } else {
        onGranted()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OkonowTimePicker(
    onTimeSelected: (LocalTime) -> Unit,
    onDismissRequest: () -> Unit,
    hazeState: HazeState
) {
    val currentTime = LocalTime.now()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = false
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeEffect(state = hazeState) {
                    backgroundColor = Background
                    blurRadius = 24.dp
                    tints = listOf(HazeTint(Color.Black.copy(alpha = 0.5f)))
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest
                )
        )

        // Content
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f)
                        )
                    )
                )
                .hazeEffect(state = hazeState) {
                    backgroundColor = Background
                    blurRadius = 40.dp
                    tints = listOf(HazeTint(Background.copy(alpha = 0.2f)))
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Time",
                    style = MaterialTheme.typography.headlineSmall,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f),
                        clockDialSelectedContentColor = Background,
                        clockDialUnselectedContentColor = OnSurface,
                        selectorColor = PrimaryPurple,
                        containerColor = Color.Transparent,
                        periodSelectorBorderColor = PrimaryPurple,
                        periodSelectorSelectedContainerColor = PrimaryPurple.copy(alpha = 0.2f),
                        periodSelectorSelectedContentColor = PrimaryPurple,
                        periodSelectorUnselectedContainerColor = Color.Transparent,
                        periodSelectorUnselectedContentColor = OnSurfaceVariant,
                        timeSelectorSelectedContainerColor = PrimaryPurple.copy(alpha = 0.2f),
                        timeSelectorSelectedContentColor = PrimaryPurple,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f),
                        timeSelectorUnselectedContentColor = OnSurface
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("CANCEL", color = OnSurfaceVariant, letterSpacing = 1.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onTimeSelected(LocalTime.of(timePickerState.hour, timePickerState.minute))
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple,
                            contentColor = Background
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("DONE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun OkonowPickerDialog(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
    hazeState: HazeState
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeEffect(state = hazeState) {
                    backgroundColor = Background
                    blurRadius = 24.dp
                    tints = listOf(HazeTint(Color.Black.copy(alpha = 0.5f)))
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest
                )
        )

        // Content
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.6f)
                        )
                    )
                )
                .hazeEffect(state = hazeState) {
                    backgroundColor = Background
                    blurRadius = 40.dp
                    tints = listOf(HazeTint(Background.copy(alpha = 0.2f)))
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    options.forEach { option ->
                        val isSelected = option == selectedOption
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .then(
                                    if (isSelected) {
                                        Modifier.background(
                                            Brush.linearGradient(
                                                listOf(PrimaryPurple, TertiaryPink)
                                            )
                                        )
                                    } else {
                                        Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.3f))
                                    }
                                )
                                .clickable {
                                    onOptionSelected(option)
                                }
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = option,
                                    color = if (isSelected) Background else OnSurface,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Background,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("CANCEL", color = OnSurfaceVariant, letterSpacing = 1.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple,
                            contentColor = Background,
                            disabledContainerColor = SurfaceVariant.copy(alpha = 0.5f),
                            disabledContentColor = OnSurface.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("DONE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
