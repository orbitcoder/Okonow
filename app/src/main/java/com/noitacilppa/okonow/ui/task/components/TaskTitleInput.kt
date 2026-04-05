package com.noitacilppa.okonow.ui.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.PrimaryPurple

@Composable
fun TaskTitleInput(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(vertical = 4.dp),
            textStyle = TextStyle(
                color = OnSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 34.sp
            ),
            cursorBrush = SolidColor(PrimaryPurple),
            interactionSource = interactionSource,
            decorationBox = { inner ->
                Box {
                    if (value.isEmpty()) {
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
                    brush = if (isFocused) {
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
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TaskTitleInputPreview() {
    TaskTitleInput(
        value = "",
        onValueChange = {},
        focusRequester = remember { FocusRequester() }
    )
}
