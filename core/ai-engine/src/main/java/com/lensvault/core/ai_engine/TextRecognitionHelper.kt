package com.lensvault.core.ai_engine

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextRecognitionHelper @Inject constructor() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun extractText(context: Context, imageUri: Uri): String {
        return withContext(Dispatchers.IO) {
            try {
                val image = InputImage.fromFilePath(context, imageUri)
                val task = recognizer.process(image)
                
                while (!task.isComplete) {
                    Thread.sleep(50)
                }

                if (task.isSuccessful) {
                    task.result.text
                } else {
                    ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}
