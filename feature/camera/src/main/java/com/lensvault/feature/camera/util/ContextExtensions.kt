package com.lensvault.feature.camera.util

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    val processCameraProviderFuture = ProcessCameraProvider.getInstance(this)
    processCameraProviderFuture.addListener({
        continuation.resume(processCameraProviderFuture.get())
    }, ContextCompat.getMainExecutor(this))
}
