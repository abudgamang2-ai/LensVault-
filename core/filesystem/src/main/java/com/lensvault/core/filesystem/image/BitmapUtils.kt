package com.lensvault.core.filesystem.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitmapUtils @Inject constructor() {

    suspend fun loadBitmap(context: Context, uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap = withContext(Dispatchers.Default) {
        val matrix = Matrix().apply { postRotate(degrees) }
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    suspend fun cropBitmap(bitmap: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap = withContext(Dispatchers.Default) {
        // Ensure crop rect is within bounds
        val safeX = x.coerceIn(0, bitmap.width)
        val safeY = y.coerceIn(0, bitmap.height)
        val safeWidth = width.coerceAtMost(bitmap.width - safeX)
        val safeHeight = height.coerceAtMost(bitmap.height - safeY)
        
        Bitmap.createBitmap(bitmap, safeX, safeY, safeWidth, safeHeight)
    }
    
    suspend fun saveBitmap(context: Context, bitmap: Bitmap): Uri? = withContext(Dispatchers.IO) {
        try {
            val filename = "edited_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename) // Save to internal storage for now
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
