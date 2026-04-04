package com.noitacilppa.okonow.ui.onboarding

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.noitacilppa.okonow.ui.components.ContinueButton
import com.noitacilppa.okonow.ui.components.ProfileImageUploader
import com.noitacilppa.okonow.ui.navigation.Screen
import com.noitacilppa.okonow.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = viewModel()
) {
    val context = LocalContext.current
    var showDiscardDialog by remember { mutableStateOf(false) }

    val handleBack = {
        if (viewModel.hasStartedEntry) {
            showDiscardDialog = true
        } else {
            (context as? android.app.Activity)?.finish()
        }
        Unit
    }

    BackHandler(onBack = handleBack)

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard changes?") },
            text = { Text("You will lose your progress if you go back.") },
            confirmButton = {
                TextButton(onClick = { (context as? android.app.Activity)?.finish() }) {
                    Text("Discard", color = TertiaryPink)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("Cancel", color = OnSurface)
                }
            },
            containerColor = SurfaceContainerHighest,
            titleContentColor = OnSurface,
            textContentColor = OnSurface
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // --- Background Gradients (Full Width) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PrimaryPurple.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, SecondaryTeal.copy(alpha = 0.1f))
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Okonow",
                            fontWeight = FontWeight.Bold,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = PrimaryPurple
                        )
                    },
                    actions = {
                        IconButton(onClick = handleBack) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = OnSurface)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // --- Hero Section ---
                Text(
                    text = buildAnnotatedString {
                        append("Create your ")
                        withStyle(style = SpanStyle(color = PrimaryPurple)) {
                            append("space.")
                        }
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Set up your profile to start managing tasks with editorial precision.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp, bottom = 48.dp)
                )

                // --- Profile Image Uploader ---
                ProfileImageUploader(
                    imageUri = viewModel.imageUri,
                    onImagePicked = { viewModel.updateImageUri(it) }

                )

                Spacer(modifier = Modifier.height(48.dp))

                // --- Input Field (Glassmorphic) ---
                Text(
                    text = "FULL NAME",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryPurple,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    letterSpacing = 0.2.sp
                )

                var isFocused by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow.copy(alpha = 0.4f))
                        .border(
                            width = if (isFocused) 2.dp else 1.dp,
                            color = if (isFocused) PrimaryPurple.copy(alpha = 0.5f) else OutlineVariant,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = viewModel.name,
                            onValueChange = { viewModel.updateName(it) },
                            placeholder = { Text("Enter your name", color = OnSurface.copy(alpha = 0.3f)) },
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged { isFocused = it.isFocused },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = PrimaryPurple,
                                focusedTextColor = OnSurface,
                                unfocusedTextColor = OnSurface
                            ),
                            singleLine = true
                        )
                        Icon(
                            Icons.Default.AlternateEmail,
                            contentDescription = null,
                            tint = if (isFocused) PrimaryPurple else OnSurface.copy(alpha = 0.2f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = buildAnnotatedString {
                        append("By continuing, you agree to our ")
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("Terms of Service")
                        }
                        append(" and ")
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("Privacy Policy")
                        }
                        append(".")
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Legal policies coming soon", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // --- Continue Button ---
                ContinueButton(
                    text = "Continue",
                    enabled = viewModel.isComplete,
                    onClick = { navController.navigate(Screen.Main.route) }
                )

                // --- Progress Indicator ---
                Row(
                    modifier = Modifier.padding(vertical = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 24.dp, height = 6.dp)
                            .clip(CircleShape)
                            .background(PrimaryPurple)
                    )
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SurfaceContainerHighest))
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SurfaceContainerHighest))
                }
            }
        }
    }
}
