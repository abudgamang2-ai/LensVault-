package com.lensvault.feature.editor.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lensvault.feature.editor.domain.EditOperation
import com.lensvault.feature.editor.domain.EditState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bitmapUtils: com.lensvault.core.filesystem.image.BitmapUtils
) : ViewModel() {

    private val _editState = MutableStateFlow(EditState(uri = ""))
    val editState: StateFlow<EditState> = _editState.asStateFlow()
    
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun setInitialImage(uri: String) {
        _editState.value = EditState(uri = uri)
    }

    fun applyEdits() {
        if (_isSaving.value) return
        
        val currentUri = _editState.value.uri
        val ops = _editState.value.operations
        
        if (ops.isEmpty()) return

        _isSaving.value = true
        
        viewModelScope.launch {
            val uri = android.net.Uri.parse(currentUri)
            var bitmap = bitmapUtils.loadBitmap(context, uri)
            
            if (bitmap != null) {
                // Apply operations sequentially
                ops.forEach { op ->
                    bitmap = when (op) {
                        is EditOperation.Rotate -> bitmapUtils.rotateBitmap(bitmap!!, op.degrees)
                        is EditOperation.Crop -> bitmapUtils.cropBitmap(bitmap!!, op.startX.toInt(), op.startY.toInt(), op.width.toInt(), op.height.toInt())
                        // Filter not implemented in v1 of BitmapUtils
                        else -> bitmap
                    }
                }
                
                // Save Result
                val newUri = bitmapUtils.saveBitmap(context, bitmap!!)
                
                if (newUri != null) {
                     // Reset state to new image, clear ops
                    _editState.value = EditState(uri = newUri.toString())
                }
            }
            _isSaving.value = false
        }
    }


    fun addOperation(op: EditOperation) {
        val currentOps = _editState.value.operations.toMutableList()
        currentOps.add(op)
        _editState.value = _editState.value.copy(
            operations = currentOps,
            redoStack = emptyList() // Clear redo stack on new operation
        )
    }

    fun undo() {
        val currentOps = _editState.value.operations.toMutableList()
        if (currentOps.isNotEmpty()) {
            val op = currentOps.removeLast()
            val currentRedo = _editState.value.redoStack.toMutableList()
            currentRedo.add(op)
            _editState.value = _editState.value.copy(
                operations = currentOps,
                redoStack = currentRedo
            )
        }
    }

    fun redo() {
        val currentRedo = _editState.value.redoStack.toMutableList()
        if (currentRedo.isNotEmpty()) {
            val op = currentRedo.removeLast()
            val currentOps = _editState.value.operations.toMutableList()
            currentOps.add(op)
            _editState.value = _editState.value.copy(
                operations = currentOps,
                redoStack = currentRedo
            )
        }
    }
}
