package com.lensvault.feature.editor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lensvault.feature.editor.domain.EditOperation
import com.lensvault.feature.editor.viewmodel.EditorViewModel

@Composable
fun EditorScreen(
    uri: String,
    viewModel: EditorViewModel = hiltViewModel()
) {
    // In a real app, apply transformations to the ImageBitmap or use a custom View
    // For now, we visualize the state interactions.
    val state by viewModel.editState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AsyncImage(
                model = uri,
                contentDescription = "Editing Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            // Overlay operations visualization (simplified)
            Text(
                text = "Operations applied: ${state.operations.size}",
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Button(onClick = { viewModel.addOperation(EditOperation.Rotate(90f)) }) {
                Text("Rotate")
            }
            Button(onClick = { viewModel.undo() }) {
                Text("Undo")
            }
            Button(onClick = { viewModel.redo() }) {
                Text("Redo")
            }
        }
    }
}
