package com.lensvault.core.domain.repository

import com.lensvault.core.model.Media

interface AiRepository {
    suspend fun analyzeMedia(media: Media)
    suspend fun search(query: String): List<Long> // Returns Media IDs
}
