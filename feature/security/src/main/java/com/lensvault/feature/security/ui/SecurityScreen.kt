package com.lensvault.feature.security.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.lensvault.feature.security.util.BiometricHelper
import com.lensvault.feature.security.viewmodel.SecurityViewModel

@Composable
fun SecurityScreen(
    viewModel: SecurityViewModel = hiltViewModel(),
    onUnlockSuccess: () -> Unit
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            onUnlockSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            if (context is FragmentActivity) {
                BiometricHelper(context) { success ->
                    if (success) viewModel.onAuthenticationSuccess()
                    else viewModel.onAuthenticationFailed()
                }.showBiometricPrompt()
            }
        }) {
            Text("Unlock Private Folder")
        }
    }
}
