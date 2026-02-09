package com.lensvault.core.model

data class Media(
    val id: Long,
    val uri: String,
    val path: String,
    val name: String,
    val dateAdded: Long,
    val mimeType: String,
    val width: Int,
    val height: Int,
    val size: Long,
    val duration: Long = 0, // 0 for images
    val isVideo: Boolean
)
