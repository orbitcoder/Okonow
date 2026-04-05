package com.noitacilppa.okonow.ui.main

import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import androidx.compose.ui.graphics.Color

val testState = HazeState()
val testMod = Modifier.haze(state = testState)
