package com.noitacilppa.okonow.ui.task.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.task.SubtaskState
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.OutlineVariant
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SurfaceContainerLow
import kotlin.math.max
import kotlin.math.min

// --- Range Utility Functions ---

fun mergeRanges(ranges: List<IntRange>): List<IntRange> {
    if (ranges.isEmpty()) return emptyList()
    val sorted = ranges.filter { !it.isEmpty() }.sortedBy { it.first }
    if (sorted.isEmpty()) return emptyList()

    val merged = mutableListOf<IntRange>()
    var current = sorted[0]

    for (i in 1 until sorted.size) {
        val next = sorted[i]
        if (next.first <= current.last + 1) {
            current = current.first..max(current.last, next.last)
        } else {
            merged.add(current)
            current = next
        }
    }
    merged.add(current)
    return merged
}

fun updateBoldRanges(
    oldRanges: List<IntRange>,
    oldText: String,
    newText: String,
    selection: TextRange,
    isBoldActive: Boolean
): List<IntRange> {
    val diff = newText.length - oldText.length
    if (diff == 0) return oldRanges

    // Approximate position of the change
    val changePos = if (diff > 0) selection.start - diff else selection.start
    val result = mutableListOf<IntRange>()

    oldRanges.forEach { range ->
        when {
            // Range is completely before the change
            range.last < changePos -> {
                result.add(range)
            }
            // Range is completely after the change
            range.first >= changePos + if (diff < 0) -diff else 0 -> {
                result.add((range.first + diff)..(range.last + diff))
            }
            // Range overlaps with the change
            else -> {
                val newStart = if (range.first >= changePos) range.first + diff else range.first
                val newEnd = range.last + diff
                
                // Adjust boundaries
                val finalStart = max(0, newStart)
                val finalEnd = min(newText.length - 1, newEnd)
                
                if (finalStart <= finalEnd) {
                    result.add(finalStart..finalEnd)
                }
            }
        }
    }

    // Apply bold to new text if bold mode is active
    if (isBoldActive && diff > 0) {
        val newRangeStart = changePos
        val newRangeEnd = changePos + diff - 1
        if (newRangeStart <= newRangeEnd) {
            result.add(newRangeStart..newRangeEnd)
        }
    }

    return mergeRanges(result)
}

fun toggleBoldOnSelection(ranges: List<IntRange>, selection: TextRange): List<IntRange> {
    if (selection.collapsed) return ranges
    val selStart = selection.min
    val selEnd = selection.max - 1
    val selRange = selStart..selEnd

    // If selection is already fully bold, un-bold it
    val isAlreadyBold = ranges.any { it.first <= selStart && it.last >= selEnd }

    return if (isAlreadyBold) {
        val result = mutableListOf<IntRange>()
        ranges.forEach { range ->
            if (range.last < selStart || range.first > selEnd) {
                result.add(range)
            } else {
                if (range.first < selStart) {
                    result.add(range.first until selStart)
                }
                if (range.last > selEnd) {
                    result.add((selEnd + 1)..range.last)
                }
            }
        }
        result
    } else {
        mergeRanges(ranges + listOf(selRange))
    }
}

fun parseBoldRanges(html: String): Pair<String, List<IntRange>> {
    val sb = StringBuilder()
    val ranges = mutableListOf<IntRange>()
    var i = 0
    var boldStart = -1
    while (i < html.length) {
        if (html.startsWith("<b>", i)) {
            boldStart = sb.length
            i += 3
        } else if (html.startsWith("</b>", i)) {
            if (boldStart != -1) {
                ranges.add(boldStart until sb.length)
                boldStart = -1
            }
            i += 4
        } else {
            sb.append(html[i])
            i++
        }
    }
    if (boldStart != -1) ranges.add(boldStart until sb.length)
    return sb.toString() to ranges
}

fun convertToHtml(text: String, boldRanges: List<IntRange>): String {
    val sb = StringBuilder()
    var last = 0
    boldRanges.sortedBy { it.first }.forEach { range ->
        if (range.first in 0..text.length && range.last < text.length) {
            sb.append(text.substring(last, range.first))
            sb.append("<b>")
            sb.append(text.substring(range.first, range.last + 1))
            sb.append("</b>")
            last = range.last + 1
        }
    }
    if (last < text.length) {
        sb.append(text.substring(last))
    }
    return sb.toString()
}

@Composable
fun TaskDescriptionInput(
    value: String,
    onValueChange: (String) -> Unit,
    onAttachmentChange: (Uri?) -> Unit = {},
    attachmentUri: Uri? = null,
    isSubtaskMode: Boolean = false,
    subtasks: List<String> = emptyList(),
    subtaskStates: List<SubtaskState> = emptyList(),
    onSubtaskChange: (Int, String) -> Unit = { _, _ -> },
    onSubtaskToggle: (Int) -> Unit = {},
    onAddSubtask: () -> Unit = {},
    onToggleMode: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Current text and bold ranges metadata
    var boldRanges by remember(value) {
        val (_, ranges) = parseBoldRanges(value)
        mutableStateOf(ranges)
    }
    
    // We use TextFieldValue for visual state and cursor management
    var internalValue by remember(value) {
        val (text, ranges) = parseBoldRanges(value)
        val annotated = buildAnnotatedString {
            append(text)
            ranges.forEach { addStyle(SpanStyle(fontWeight = FontWeight.Bold), it.first, it.last + 1) }
        }
        mutableStateOf(TextFieldValue(annotated, TextRange(text.length)))
    }

    var isBoldActive by remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onAttachmentChange(uri)
    }

    fun updateInternalState(newValue: TextFieldValue, newRanges: List<IntRange>) {
        boldRanges = newRanges
        val annotated = buildAnnotatedString {
            append(newValue.text)
            newRanges.forEach { 
                if (it.first in 0..newValue.text.length && it.last < newValue.text.length) {
                    addStyle(SpanStyle(fontWeight = FontWeight.Bold), it.first, it.last + 1)
                }
            }
        }
        internalValue = newValue.copy(annotatedString = annotated)
        onValueChange(convertToHtml(newValue.text, newRanges))
    }

    fun handleValueChange(newValue: TextFieldValue) {
        val oldText = internalValue.text
        val newText = newValue.text

        if (newText != oldText) {
            val nextRanges = updateBoldRanges(boldRanges, oldText, newText, newValue.selection, isBoldActive)
            updateInternalState(newValue, nextRanges)
        } else {
            internalValue = newValue
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLow.copy(alpha = 0.4f))
            .border(1.dp, OutlineVariant.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = onToggleMode) {
                Icon(
                    Icons.Default.FormatListBulleted,
                    null,
                    tint = if (isSubtaskMode) PrimaryPurple else OnSurfaceVariant
                )
            }
            IconButton(onClick = { 
                if (internalValue.selection.collapsed) {
                    isBoldActive = !isBoldActive
                } else {
                    val nextRanges = toggleBoldOnSelection(boldRanges, internalValue.selection)
                    updateInternalState(internalValue, nextRanges)
                }
            }) {
                Icon(
                    Icons.Default.FormatBold, 
                    null, 
                    tint = if (isBoldActive) PrimaryPurple else OnSurfaceVariant
                )
            }
            IconButton(onClick = { filePickerLauncher.launch("*/*") }) {
                Icon(
                    Icons.Default.AttachFile, 
                    null, 
                    tint = if (attachmentUri != null) PrimaryPurple else OnSurfaceVariant
                )
            }
        }

        if (attachmentUri != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.AttachFile, null, tint = PrimaryPurple, modifier = Modifier.size(16.dp))
                Text(
                    text = attachmentUri.lastPathSegment ?: "File attached",
                    fontSize = 12.sp,
                    color = PrimaryPurple,
                    maxLines = 1
                )
                IconButton(
                    onClick = { onAttachmentChange(null) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp)) // Using Add as a placeholder for clear
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(OutlineVariant.copy(alpha = 0.15f))
        )
        
        if (isSubtaskMode) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subtaskStates.forEachIndexed { index, state ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = state.isDone,
                            onCheckedChange = { onSubtaskToggle(index) },
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = OnSurfaceVariant.copy(alpha = 0.5f),
                                checkmarkColor = PrimaryPurple
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                        BasicTextField(
                            value = state.description,
                            onValueChange = { onSubtaskChange(index, it) },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                color = OnSurface.copy(alpha = if (state.isDone) 0.4f else 0.85f),
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                textDecoration = if (state.isDone) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            cursorBrush = SolidColor(PrimaryPurple),
                            decorationBox = { inner ->
                                Box {
                                    if (state.description.isEmpty()) {
                                        Text(
                                            "Subtask description",
                                            fontSize = 16.sp,
                                            color = OnSurface.copy(alpha = 0.2f)
                                        )
                                    }
                                    inner()
                                }
                            }
                        )
                    }
                }
                IconButton(
                    onClick = onAddSubtask,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(Icons.Default.Add, "Add Subtask", tint = PrimaryPurple)
                }
            }
        } else {
            BasicTextField(
                value = internalValue,
                onValueChange = { handleValueChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp),
                textStyle = TextStyle(
                    color = OnSurface.copy(alpha = 0.85f),
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                ),
                cursorBrush = SolidColor(PrimaryPurple),
                decorationBox = { inner ->
                    Box {
                        if (internalValue.text.isEmpty()) {
                            Text(
                                "Add tasks description here",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface.copy(alpha = 0.2f)
                            )
                        }
                        inner()
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TaskDescriptionInputPreview() {
    TaskDescriptionInput(
        value = "Focus on the <b>quarterly review</b> preparations.",
        onValueChange = {}
    )
}
