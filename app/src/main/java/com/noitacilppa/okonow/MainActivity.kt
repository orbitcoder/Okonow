package com.noitacilppa.okonow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.noitacilppa.okonow.ui.navigation.OkonowNavHost
import com.noitacilppa.okonow.ui.theme.OkonowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OkonowTheme {
                OkonowNavHost()
            }
        }
    }
}
