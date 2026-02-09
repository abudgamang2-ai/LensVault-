package com.lensvault.feature.editor.viewmodel

import androidx.lifecycle.ViewModel
import com.lensvault.feature.editor.domain.EditOperation
import com.lensvault.feature.editor.domain.EditState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor() : ViewModel() {

    private val _editState = MutableStateFlow(EditState(uri = ""))
    val editState: StateFlow<EditState> = _editState.asStateFlow()

    fun setInitialImage(uri: String) {
        _editState.value = EditState(uri = uri)
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
