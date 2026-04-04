package com.noitacilppa.okonow.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.noitacilppa.okonow.ui.theme.*

@Composable
fun ProfileImageUploader(
    imageUri: Uri?,
    onImagePicked: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> onImagePicked(uri) }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Main Circle Avatar with Gradient Border
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh)
                .border(2.dp, Brush.linearGradient(listOf(PrimaryPurple, TertiaryPink)), CircleShape)
                .clickable {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Overlay
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(if (imageUri != null) Color.Black.copy(alpha = 0.4f) else Color.Transparent)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = if (imageUri != null) Color.White.copy(alpha = 0.8f) else PrimaryPurple.copy(alpha = 0.6f),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "UPLOAD PHOTO",
                    color = if (imageUri != null) Color.White.copy(alpha = 0.8f) else OnSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }
}

@Composable
fun ContinueButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "press")
    
    val arrowOffset by animateFloatAsState(
        targetValue = if (isPressed) 8f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "arrow"
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        // Background Glow
        if (enabled) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(40.dp)
                    .offset(y = 20.dp)
                    .blur(30.dp)
                    .background(PrimaryPurple.copy(alpha = 0.4f), RoundedCornerShape(9999.dp))
            )
        }

        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .scale(scale),
            interactionSource = interactionSource,
            shape = RoundedCornerShape(9999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContainerColor = SurfaceVariant.copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.3f)
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (enabled) Brush.linearGradient(listOf(PrimaryPurple, TertiaryPink))
                        else Brush.linearGradient(listOf(SurfaceVariant, SurfaceVariant))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text,
                        color = if (enabled) Background else OnSurface.copy(alpha = 0.3f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.offset(x = arrowOffset.dp),
                        tint = if (enabled) Background else OnSurface.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileImageUploaderPreview() {
    OkonowTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileImageUploader(
                imageUri = null,
                onImagePicked = {},
                modifier = Modifier.padding(40.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContinueButtonPreview() {
    OkonowTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(40.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                ContinueButton(
                    text = "Continue",
                    enabled = true,
                    onClick = {}
                )
                ContinueButton(
                    text = "Continue",
                    enabled = false,
                    onClick = {}
                )
            }
        }
    }
}
