package com.lensvault.feature.gallery.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.lensvault.core.model.Media
import com.lensvault.feature.gallery.viewmodel.GalleryViewModel

@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = hiltViewModel(),
    onMediaClick: (Media) -> Unit
) {
    val mediaItems: LazyPagingItems<Media> = viewModel.mediaPagingFlow.collectAsLazyPagingItems()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(mediaItems.itemCount) { index ->
                val media = mediaItems[index]
                if (media != null) {
                    GalleryItem(media = media, onClick = { onMediaClick(media) })
                }
            }
        }
    }
}

@Composable
fun GalleryItem(
    media: Media,
    onClick: () -> Unit
) {
    AsyncImage(
        model = media.uri,
        contentDescription = media.name,
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}
