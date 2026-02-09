package com.lensvault.core.ai_engine

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaceDetectorHelper @Inject constructor() {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL) // Uncomment if landmarks needed
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .build()

    private val detector = FaceDetection.getClient(options)

    suspend fun detectFaces(context: Context, imageUri: Uri): List<Face> {
        return withContext(Dispatchers.IO) {
            try {
                val image = InputImage.fromFilePath(context, imageUri)
                // ML Kit's process method is async but we want to coroutine-suspend it
                // Converting Task to suspend function manually or using library
                val task = detector.process(image)
                
                // Simple blocking wait for now as we are already on IO dispatcher
                // or use task.await() if available in dependencies
                while (!task.isComplete) {
                     Thread.sleep(50)
                }
                
                if (task.isSuccessful) {
                    task.result
                } else {
                    emptyList()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
