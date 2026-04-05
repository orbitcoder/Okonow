package com.noitacilppa.okonow.ui.main

import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import androidx.compose.ui.graphics.Color
import com.noitacilppa.okonow.ui.theme.Background

val testState = HazeState()
val testMod = Modifier.hazeEffect(state = testState) {
    backgroundColor = Background
}
