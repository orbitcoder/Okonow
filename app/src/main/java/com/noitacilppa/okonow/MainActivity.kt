package com.noitacilppa.okonow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.noitacilppa.okonow.data.UserPreferences
import com.noitacilppa.okonow.ui.navigation.OkonowNavHost
import com.noitacilppa.okonow.ui.theme.OkonowTheme
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private var initialUserName: String? = null
    private var isDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        val userPreferences = UserPreferences(this)
        
        // Keep the system splash screen on screen until we have the initial data from DataStore
        splashScreen.setKeepOnScreenCondition { !isDataLoaded }

        lifecycleScope.launch {
            // Fetch the username while the splash is showing
            initialUserName = userPreferences.userName.first()
            isDataLoaded = true
        }

        var showCustomSplash by mutableStateOf(true)

        enableEdgeToEdge()
        setContent {
            OkonowTheme {
                if (showCustomSplash) {
                    SplashScreenContent(onAnimationFinished = {
                        showCustomSplash = false
                    })
                } else {
                    OkonowNavHost(initialUserName = initialUserName)
                }
            }
        }
    }
}

@Composable
fun SplashScreenContent(onAnimationFinished: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        // 1. Scale up the icon
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(durationMillis = 600)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 200)
        )
        
        delay(200)
        
        // 2. Move icon up and fade in text simultaneously
        val animDuration = 600
        launch {
            offsetY.animateTo(
                targetValue = (-40).dp.value,
                animationSpec = tween(durationMillis = animDuration)
            )
        }
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animDuration)
        )
        
        delay(1200)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E10)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .offset(y = offsetY.value.dp)
                    .scale(scale.value)
            )
            
            Text(
                text = "OKONOW",
                color = PrimaryPurple,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 8.sp,
                modifier = Modifier
                    .offset(y = offsetY.value.dp)
                    .alpha(textAlpha.value)
            )
        }
    }
}
