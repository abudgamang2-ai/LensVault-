package com.lensvault.feature.security.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class BiometricHelper(
    private val activity: FragmentActivity,
    private val onResult: (Boolean) -> Unit
) {
    private val executor: Executor = ContextCompat.getMainExecutor(activity)

    fun showBiometricPrompt() {
        val biometricManager = BiometricManager.from(activity)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) != BiometricManager.BIOMETRIC_SUCCESS) {
            onResult(false) // Or handle other error codes
            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("LensVault Security")
            .setSubtitle("Confirm your identity to access private folder")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onResult(true)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onResult(false)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onResult(false)
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}
