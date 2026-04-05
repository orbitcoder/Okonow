package com.noitacilppa.okonow.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.widthIn
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

/** Default avatar fallback if user hasn't set one. */
private const val DefaultAvatarUrl =
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
    val userImageUri by userPreferences.userImageUri.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    
    var themeModeIndex by rememberSaveable { mutableIntStateOf(0) }
    var doNotDisturb by rememberSaveable { mutableStateOf(true) }
    var hapticFeedback by rememberSaveable { mutableStateOf(true) }
    var highPerformance by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 600.dp
        val horizontalPadding = if (isTablet) 48.dp else 24.dp
        val avatarSize = (maxWidth * 0.25f).coerceIn(100.dp, 156.dp)

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background.copy(alpha = 0.92f))
                    .padding(top = 8.dp, bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = if (isTablet) Modifier.widthIn(max = 800.dp) else Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                ProfileHeroSection(
                    name = userName ?: "Guest", 
                    avatarUri = userImageUri,
                    avatarSize = avatarSize
                )
                
                InteractionsSection(
                    doNotDisturb = doNotDisturb,
                    onDoNotDisturb = { doNotDisturb = it },
                    hapticFeedback = hapticFeedback,
                    onHapticFeedback = { hapticFeedback = it },
                    highPerformance = highPerformance,
                    onHighPerformance = { highPerformance = it },
                    modifier = if (isTablet) Modifier.widthIn(max = 600.dp) else Modifier.fillMaxWidth()
                )
                
                StatsRow(
                    modifier = if (isTablet) Modifier.widthIn(max = 800.dp) else Modifier.fillMaxWidth()
                )
                
                SignOutButton(
                    onClick = {
                        scope.launch {
                            userPreferences.clear()
                            todoViewModel.logout { }
                        }
                    },
                    modifier = if (isTablet) Modifier.widthIn(max = 400.dp) else Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ProfileHeroSection(name: String, avatarUri: String?, avatarSize: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            HeroGlowPurple.copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        center = Offset(size.width / 2f, 0f),
                        radius = size.width * 0.8f
                    )
                )
            }
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .size(avatarSize)
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
                            model = avatarUri ?: DefaultAvatarUrl,
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
                    color = OnSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Deep Focus Strategist",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(label: String) {
    Text(
        label,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = OnSurfaceVariant,
        letterSpacing = 1.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun InteractionsSection(
    doNotDisturb: Boolean,
    onDoNotDisturb: (Boolean) -> Unit,
    hapticFeedback: Boolean,
    onHapticFeedback: (Boolean) -> Unit,
    highPerformance: Boolean,
    onHighPerformance: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionLabel("Interactions")
        
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(ProfileCardCorner))
                .background(SurfaceContainerLow)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            InteractionRow(
                icon = Icons.Default.DoNotDisturbOn,
                label = "Focus Mode (DND)",
                checked = doNotDisturb,
                onCheckedChange = onDoNotDisturb
            )
            InteractionRow(
                icon = Icons.Default.Vibration,
                label = "Haptic Feedback",
                checked = hapticFeedback,
                onCheckedChange = onHapticFeedback
            )
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
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryPurple)
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = OnSurface,
            modifier = Modifier.weight(1f)
        )
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
private fun StatsRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            icon = Icons.Default.TaskAlt,
            count = "128",
            label = "Completed",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Default.HourglassEmpty,
            count = "42h",
            label = "Deep Work",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(ProfileCardCorner))
            .background(SurfaceContainerLow)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(28.dp))
        Text(count, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = OnSurface)
        Text(label, style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant)
    }
}

@Composable
private fun SignOutButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TertiaryPink
        ),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TertiaryPink.copy(alpha = 0.3f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Text("Sign Out", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
