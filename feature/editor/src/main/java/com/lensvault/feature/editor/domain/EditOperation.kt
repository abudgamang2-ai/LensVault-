package com.lensvault.feature.editor.domain

sealed interface EditOperation {
    data class Crop(val startX: Float, val startY: Float, val width: Float, val height: Float) : EditOperation
    data class Rotate(val degrees: Float) : EditOperation
    data class Filter(val filterName: String) : EditOperation
}

data class EditState(
    val uri: String,
    val operations: List<EditOperation> = emptyList(),
    val redoStack: List<EditOperation> = emptyList()
)
