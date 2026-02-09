package com.lensvault.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lensvault.core.ui.theme.LensVaultTheme
import com.lensvault.feature.camera.ui.CameraScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LensVaultTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LensVaultApp()
                }
            }
        }
    }
}

@Composable
fun LensVaultApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "camera") {
        composable("camera") {
            CameraScreen(
                onNavigateToGallery = { 
                    // Navigate to gallery when implemented
                    // navController.navigate("gallery") 
                }
            )
        }
        // Add other destinations here
    }
}
