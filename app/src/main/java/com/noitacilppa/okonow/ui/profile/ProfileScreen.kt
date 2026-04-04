package com.noitacilppa.okonow.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.noitacilppa.okonow.data.UserPreferences
import com.noitacilppa.okonow.ui.AppViewModelProvider
import com.noitacilppa.okonow.ui.TodoViewModel
import com.noitacilppa.okonow.ui.theme.*
import kotlinx.coroutines.launch

/** Unified corner radius for profile cards (theme previews, interactions panel, stats) — matches design ~24dp. */
private val ProfileCardCorner = 32.dp

/** Radial highlight behind hero — Stitch `.glossy-header`: circle at top center, purple 15% → transparent. */
private val HeroGlowPurple = Color(0xFF9D50FF)

/** Stitch export + FIFE reference for profile hero (same screen as design). */
private const val StitchProfileAvatarUrl =
    "https://lh3.googleusercontent.com/aida-public/AB6AXuDm9Nls_6jdH7TDAwVZ9jKI7J0aFhgTJicpkzDAVSTrxb0vz1KdLHirS2GI5mj62lO7iOO9sYydNs_e41a-gGuqwLTEWdVia6QM8SNoZTig74YTQyNxl0EQWkTGG8M_4k7ammZDatveujfCVfEzEJYJxCaPhDhKOKkeJVsgPheSepYPlRM579F6t35Gn54s4FUdtxDwyJCKKLMJeh6svXrRHEOQKa3OgO3YO1tSh1ZY5UcfcovZBM3RzbCliVgZr8dMlxzo71wgi_s"

private enum class ThemeModeOption(val label: String) {
    OnyxGlass("Onyx Glass"),
    SapphireGlow("Sapphire Glow"),
    EmeraldShimmer("Emerald Shimmer")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userName by userPreferences.userName.collectAsState(initial = "")
    val scope = rememberCoroutineScope()
    
    var themeModeIndex by rememberSaveable { mutableIntStateOf(0) }
    var doNotDisturb by rememberSaveable { mutableStateOf(true) }
    var hapticFeedback by rememberSaveable { mutableStateOf(true) }
    var highPerformance by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    "Okonow",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
            },
            actions = {
                IconButton(onClick = {
                    Toast.makeText(context, "Settings coming soon", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = OnSurface.copy(alpha = 0.6f)
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(2.dp, PrimaryContainer.copy(alpha = 0.9f), CircleShape)
                ) {
                    AsyncImage(
                        model = StitchProfileAvatarUrl,
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Background.copy(alpha = 0.92f)
            )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            ProfileHeroSection(name = userName ?: "Guest")
            ThemeModeSection(
                selectedIndex = themeModeIndex,
                onSelect = { themeModeIndex = it }
            )
            InteractionsSection(
                doNotDisturb = doNotDisturb,
                onDoNotDisturb = { doNotDisturb = it },
                hapticFeedback = hapticFeedback,
                onHapticFeedback = { hapticFeedback = it },
                highPerformance = highPerformance,
                onHighPerformance = { highPerformance = it }
            )
            StatsRow()
            SignOutButton(
                onClick = {
                    scope.launch {
                        userPreferences.clear()
                        todoViewModel.logout {
                            // Navigation logic is handled in OkonowNavHost
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileHeroSection(name: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                HeroGlowPurple.copy(alpha = 0.15f),
                                HeroGlowPurple.copy(alpha = 0.05f),
                                Color.Transparent
                            ),
                            center = Offset(size.width / 2f, 0f),
                            radius = size.width * 0.72f
                        )
                    )
                }
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .size(136.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(PrimaryPurple, TertiaryPink, SecondaryTeal)
                            ),
                            shape = CircleShape
                        )
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(4.dp, Background, CircleShape)
                    ) {
                        AsyncImage(
                            model = StitchProfileAvatarUrl,
                            contentDescription = "User avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .offset(y = 12.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(SurfaceContainerHighest)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        "15 Day Streak! 🔥",
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnSurface
                )
                Text(
                    "Deep Focus Strategist",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = OnSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ThemeModeSection(selectedIndex: Int, onSelect: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        SectionLabel("Theme Mode")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ThemeModeOption.entries.forEachIndexed { index, option ->
                ThemeModeCard(
                    label = option.label,
                    selected = index == selectedIndex,
                    accentBrush = when (index) {
                        1 -> Brush.linearGradient(listOf(Color(0xFF3B82F6), Color(0xFF6366F1)))
                        2 -> Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF34D399)))
                        else -> Brush.linearGradient(listOf(PrimaryPurple, TertiaryPink))
                    },
                    modifier = Modifier.weight(1f),
                    onClick = { onSelect(index) }
                )
            }
        }
    }
}

@Composable
private fun ThemeModeCard(
    label: String,
    selected: Boolean,
    accentBrush: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) SurfaceContainerHighest else SurfaceContainerLow)
            .border(
                width = 1.5.dp,
                brush = if (selected) accentBrush else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = accentBrush,
                    alpha = if (selected) 1f else 0.15f
                )
        )
        Text(
            label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) OnSurface else OnSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun InteractionsSection(
    doNotDisturb: Boolean,
    onDoNotDisturb: (Boolean) -> Unit,
    hapticFeedback: Boolean,
    onHapticFeedback: (Boolean) -> Unit,
    highPerformance: Boolean,
    onHighPerformance: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        SectionLabel("Interactions")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(ProfileCardCorner))
                .background(SurfaceContainerLow)
                .padding(8.dp)
        ) {
            InteractionRow(
                icon = Icons.Default.DoNotDisturbOn,
                label = "Do Not Disturb",
                checked = doNotDisturb,
                onCheckedChange = onDoNotDisturb
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = OutlineVariant.copy(alpha = 0.1f))
            InteractionRow(
                icon = Icons.Default.Vibration,
                label = "Haptic Feedback",
                checked = hapticFeedback,
                onCheckedChange = onHapticFeedback
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = OutlineVariant.copy(alpha = 0.1f))
            InteractionRow(
                icon = Icons.Default.RocketLaunch,
                label = "High Performance",
                checked = highPerformance,
                onCheckedChange = onHighPerformance
            )
        }
    }
}

@Composable
private fun InteractionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(24.dp))
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, color = OnSurface)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryPurple,
                uncheckedThumbColor = OnSurfaceVariant,
                uncheckedTrackColor = SurfaceContainerHighest
            )
        )
    }
}

@Composable
private fun StatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            label = "Completed",
            value = "128",
            icon = Icons.Default.TaskAlt,
            color = SecondaryTeal,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = "Focused",
            value = "42h",
            icon = Icons.Default.HourglassEmpty,
            color = PrimaryPurple,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(ProfileCardCorner))
            .background(SurfaceContainerLow)
            .padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = OnSurface)
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Black,
        color = OnSurface.copy(alpha = 0.4f),
        letterSpacing = 2.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Composable
private fun SignOutButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ProfileCardCorner))
            .background(SurfaceContainerLow)
            .clickable(onClick = onClick)
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = ErrorCoral, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text("Sign Out", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ErrorCoral)
    }
}
