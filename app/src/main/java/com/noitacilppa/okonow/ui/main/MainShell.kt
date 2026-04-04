package com.noitacilppa.okonow.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noitacilppa.okonow.ui.profile.ProfileScreen
import com.noitacilppa.okonow.ui.theme.Background
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.PrimaryPurple

private enum class MainTab(
    val saveKey: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    TASKS("tasks", "Tasks", Icons.Default.Checklist),
    FOCUS("focus", "Focus", Icons.Default.Timer),
    HISTORY("history", "History", Icons.Default.History),
    PROFILE("profile", "Profile", Icons.Default.Person)
}

@Composable
fun MainShell(modifier: Modifier = Modifier) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.TASKS.saveKey) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Background,
        bottomBar = {
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
                            onClick = { selectedTab = tab.saveKey },
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
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Background)
        ) {
            when (selectedTab) {
                MainTab.TASKS.saveKey -> TabPlaceholder(title = "Tasks")
                MainTab.FOCUS.saveKey -> TabPlaceholder(title = "Focus")
                MainTab.HISTORY.saveKey -> TabPlaceholder(title = "History")
                MainTab.PROFILE.saveKey -> ProfileScreen(Modifier.fillMaxSize())
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
