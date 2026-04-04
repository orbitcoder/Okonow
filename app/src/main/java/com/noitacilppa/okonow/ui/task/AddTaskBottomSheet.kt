package com.noitacilppa.okonow.ui.task

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.Background
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.OutlineVariant
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SecondaryTeal
import com.noitacilppa.okonow.ui.theme.SurfaceContainer
import com.noitacilppa.okonow.ui.theme.SurfaceContainerHighest
import com.noitacilppa.okonow.ui.theme.SurfaceContainerLow
import com.noitacilppa.okonow.ui.theme.TertiaryPink
import kotlinx.coroutines.delay

private val PrimaryDim = Color(0xFF9547F7)
private val OnPrimaryContainer = Color(0xFF330066)

private val SheetTopRadius = 24.dp

/**
 * Stitch "Add / Edit Task" ([project](https://stitch.withgoogle.com/projects/14788445287194260913)):
 * modal scrim, glass bottom sheet, NL title, AI chips, detail pills, notes surface, Save + Delete.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTaskBottomSheet(
    onDismiss: () -> Unit,
    onSave: (title: String, description: String) -> Unit
) {
    BackHandler(onBack = onDismiss)

    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember {
        mutableStateOf(
            "Focus on the quarterly review preparations. Don't forget to include the Q3 financial results and the hiring roadmap for 2024."
        )
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val sheetMaxHeight = screenHeight * 0.92f

    val titleInteraction = remember { MutableInteractionSource() }
    val titleFocused by titleInteraction.collectIsFocusedAsState()
    val titleFocusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(80)
        titleFocusRequester.requestFocus()
        keyboard?.show()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetMaxHeight)
                    .clip(RoundedCornerShape(topStart = SheetTopRadius, topEnd = SheetTopRadius))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                SurfaceContainer.copy(alpha = 0.92f),
                                Background.copy(alpha = 0.97f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        OutlineVariant.copy(alpha = 0.12f),
                        RoundedCornerShape(topStart = SheetTopRadius, topEnd = SheetTopRadius)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 40.dp, y = 96.dp)
                        .size(256.dp)
                        .background(PrimaryPurple.copy(alpha = 0.15f), CircleShape)
                        .blur(80.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-40).dp, y = (-200).dp)
                        .size(256.dp)
                        .background(SecondaryTeal.copy(alpha = 0.12f), CircleShape)
                        .blur(80.dp)
                )

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(OutlineVariant.copy(alpha = 0.4f))
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "New Task",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.05f))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = OnSurfaceVariant)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 32.dp)
                            .padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        Column {
                            BasicTextField(
                                value = titleText,
                                onValueChange = { titleText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(titleFocusRequester)
                                    .padding(vertical = 4.dp),
                                textStyle = TextStyle(
                                    color = OnSurface,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 34.sp
                                ),
                                cursorBrush = SolidColor(PrimaryPurple),
                                interactionSource = titleInteraction,
                                decorationBox = { inner ->
                                    Box {
                                        if (titleText.isEmpty()) {
                                            Text(
                                                "Call Sarah at 3 PM tomorrow",
                                                fontSize = 28.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = OnSurface.copy(alpha = 0.2f)
                                            )
                                        }
                                        inner()
                                    }
                                }
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(
                                        brush = if (titleFocused) {
                                            Brush.horizontalGradient(
                                                listOf(PrimaryPurple.copy(alpha = 0.5f), Color.Transparent)
                                            )
                                        } else {
                                            Brush.horizontalGradient(
                                                listOf(Color.Transparent, Color.Transparent)
                                            )
                                        }
                                    )
                            )
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SuggestionChip(
                                icon = { Icon(Icons.Outlined.AutoAwesome, null, tint = PrimaryPurple, modifier = Modifier.size(16.dp)) },
                                label = "Remind me 10 mins before",
                                border = PrimaryPurple.copy(alpha = 0.1f),
                                background = PrimaryPurple.copy(alpha = 0.05f),
                                textColor = PrimaryPurple
                            )
                            SuggestionChip(
                                icon = { Icon(Icons.Default.CalendarMonth, null, tint = SecondaryTeal, modifier = Modifier.size(16.dp)) },
                                label = "Repeat every Monday",
                                border = SecondaryTeal.copy(alpha = 0.1f),
                                background = SecondaryTeal.copy(alpha = 0.05f),
                                textColor = SecondaryTeal
                            )
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DetailPill(
                                icon = { Icon(Icons.Default.Event, null, tint = PrimaryPurple, modifier = Modifier.size(22.dp)) },
                                text = "Tomorrow, 3:00 PM"
                            )
                            DetailPill(
                                icon = { Icon(Icons.Default.Label, null, tint = TertiaryPink, modifier = Modifier.size(22.dp)) },
                                text = "Work"
                            )
                            DetailPill(
                                icon = { Icon(Icons.Default.PriorityHigh, null, tint = SecondaryTeal, modifier = Modifier.size(22.dp)) },
                                text = "High Priority"
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(SurfaceContainerLow.copy(alpha = 0.4f))
                                .border(1.dp, OutlineVariant.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.FormatListBulleted, null, tint = OnSurfaceVariant)
                                }
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.FormatBold, null, tint = OnSurfaceVariant)
                                }
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.AttachFile, null, tint = OnSurfaceVariant)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(OutlineVariant.copy(alpha = 0.15f))
                            )
                            BasicTextField(
                                value = descriptionText,
                                onValueChange = { descriptionText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp),
                                textStyle = TextStyle(
                                    color = OnSurface.copy(alpha = 0.85f),
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp
                                ),
                                cursorBrush = SolidColor(PrimaryPurple)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Background.copy(alpha = 0.95f))
                                )
                            )
                            .padding(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Brush.linearGradient(listOf(PrimaryPurple, PrimaryDim)))
                                    .clickable {
                                        onSave(titleText, descriptionText)
                                        onDismiss()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = OnPrimaryContainer,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        "Save Task",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = OnPrimaryContainer
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(SurfaceContainerHighest)
                                    .border(1.dp, OutlineVariant.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                                    .clickable { onDismiss() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = OnSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionChip(
    icon: @Composable () -> Unit,
    label: String,
    border: Color,
    background: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(background)
            .border(1.dp, border, RoundedCornerShape(999.dp))
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}

@Composable
private fun DetailPill(
    icon: @Composable () -> Unit,
    text: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(SurfaceContainerHighest.copy(alpha = 0.5f))
            .border(1.dp, OutlineVariant.copy(alpha = 0.12f), RoundedCornerShape(999.dp))
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = OnSurface)
    }
}
