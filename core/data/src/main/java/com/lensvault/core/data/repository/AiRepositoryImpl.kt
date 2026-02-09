package com.lensvault.core.data.repository

import android.content.Context
import android.net.Uri
import com.lensvault.core.ai_engine.FaceDetectorHelper
import com.lensvault.core.ai_engine.TextRecognitionHelper
import com.lensvault.core.ai_index.dao.AiMetadataDao
import com.lensvault.core.ai_index.model.AiMetadataEntity
import com.lensvault.core.domain.repository.AiRepository
import com.lensvault.core.model.Media
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val faceDetector: FaceDetectorHelper,
    private val textRecognizer: TextRecognitionHelper,
    private val aiMetadataDao: AiMetadataDao
) : AiRepository {

    override suspend fun analyzeMedia(media: Media) {
        val uri = Uri.parse(media.uri)
        
        // 1. Detect Faces
        val faces = faceDetector.detectFaces(context, uri)
        
        // 2. Extract Text
        val text = textRecognizer.extractText(context, uri)
        
        // 3. Save to Index
        val entity = AiMetadataEntity(
            mediaId = media.id,
            faceCount = faces.size,
            extractedText = text
        )
        aiMetadataDao.insertMetadata(entity)
    }

    override suspend fun search(query: String): List<Long> {
        return aiMetadataDao.searchByText(query).map { it.mediaId }
    }
}
