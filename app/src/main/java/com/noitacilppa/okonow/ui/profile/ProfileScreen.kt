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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.noitacilppa.okonow.ui.theme.Background
import com.noitacilppa.okonow.ui.theme.ErrorCoral
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.OutlineVariant
import com.noitacilppa.okonow.ui.theme.PrimaryContainer
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SecondaryTeal
import com.noitacilppa.okonow.ui.theme.SurfaceContainer
import com.noitacilppa.okonow.ui.theme.SurfaceContainerHigh
import com.noitacilppa.okonow.ui.theme.SurfaceContainerHighest
import com.noitacilppa.okonow.ui.theme.SurfaceContainerLow
import com.noitacilppa.okonow.ui.theme.TertiaryPink

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
fun ProfileScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
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
            ProfileHeroSection()
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
                    Toast.makeText(context, "Sign out (not wired yet)", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun ProfileHeroSection() {
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
                    "Alex Sterling",
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
                        1 -> Brush.linearGradient(listOf(Color(0xFF3B82F6).copy(alpha = 0.15f), Color.Transparent))
                        2 -> Brush.linearGradient(listOf(Color(0xFF10B981).copy(alpha = 0.15f), Color.Transparent))
                        else -> Brush.linearGradient(listOf(Color.White.copy(alpha = 0.1f), Color.Transparent))
                    },
                    onClick = { onSelect(index) },
                    modifier = Modifier.weight(1f)
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val previewShape = RoundedCornerShape(ProfileCardCorner)
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .then(if (selected) Modifier.padding(4.dp) else Modifier)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(previewShape)
                    .background(SurfaceContainerHigh)
                    .border(
                        width = if (selected) 2.dp else 1.dp,
                        color = if (selected) PrimaryPurple
                        else OutlineVariant.copy(alpha = 0.4f),
                        shape = previewShape
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(accentBrush)
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(OnSurfaceVariant.copy(alpha = if (selected) 0.2f else 0.12f))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.55f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(OnSurfaceVariant.copy(alpha = if (selected) 0.2f else 0.12f))
                    )
                }
            }
        }
        Text(
            label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = if (selected) PrimaryPurple else OnSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        SectionLabel("Interactions")
        val panelShape = RoundedCornerShape(ProfileCardCorner)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(panelShape)
                .background(SurfaceContainerLow)
        ) {
            InteractionRow(
                title = "Do Not Disturb",
                subtitle = "Auto-mute during focus sessions",
                icon = {
                    Icon(
                        Icons.Default.DoNotDisturbOn,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(22.dp)
                    )
                },
                iconContainerColor = PrimaryPurple.copy(alpha = 0.1f),
                checked = doNotDisturb,
                onCheckedChange = onDoNotDisturb,
                trackChecked = PrimaryPurple
            )
            HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 1.dp)
            InteractionRow(
                title = "Haptic Feedback",
                subtitle = "Tactile response on task completion",
                icon = {
                    Icon(
                        Icons.Default.Vibration,
                        contentDescription = null,
                        tint = SecondaryTeal,
                        modifier = Modifier.size(22.dp)
                    )
                },
                iconContainerColor = SecondaryTeal.copy(alpha = 0.1f),
                checked = hapticFeedback,
                onCheckedChange = onHapticFeedback,
                trackChecked = SecondaryTeal
            )
            HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 1.dp)
            InteractionRow(
                title = "High Performance",
                subtitle = "Optimized refresh rate for dashboard",
                icon = {
                    Icon(
                        Icons.Default.RocketLaunch,
                        contentDescription = null,
                        tint = TertiaryPink,
                        modifier = Modifier.size(22.dp)
                    )
                },
                iconContainerColor = TertiaryPink.copy(alpha = 0.1f),
                checked = highPerformance,
                onCheckedChange = onHighPerformance,
                trackChecked = TertiaryPink
            )
        }
    }
}

@Composable
private fun InteractionRow(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    iconContainerColor: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    trackChecked: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.05f), Color.Transparent)
                )
            )
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, color = OnSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, color = OnSurfaceVariant, fontSize = 12.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                uncheckedThumbColor = Color.White,
                checkedTrackColor = trackChecked,
                uncheckedTrackColor = SurfaceContainerHighest,
                uncheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent
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
            icon = { Icon(Icons.Default.TaskAlt, null, tint = PrimaryPurple, modifier = Modifier.size(32.dp)) },
            value = "412",
            label = "Tasks Completed",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = { Icon(Icons.Default.HourglassEmpty, null, tint = SecondaryTeal, modifier = Modifier.size(32.dp)) },
            value = "128h",
            label = "Focused Time",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    icon: @Composable () -> Unit,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(ProfileCardCorner)
    Column(
        modifier = modifier
            .clip(shape)
            .background(SurfaceContainerHigh)
            .border(1.dp, OutlineVariant.copy(alpha = 0.15f), shape)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        icon()
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = OnSurface
            )
            Text(
                label.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = OnSurfaceVariant
            )
        }
    }
}

@Composable
private fun SignOutButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .border(1.dp, ErrorCoral.copy(alpha = 0.35f), RoundedCornerShape(999.dp))
            .background(SurfaceContainer)
            .clickable(onClick = onClick)
            .padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.AutoMirrored.Filled.Logout,
            contentDescription = null,
            tint = ErrorCoral,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Sign Out of Okonow",
            color = ErrorCoral,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        color = OnSurfaceVariant,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}
