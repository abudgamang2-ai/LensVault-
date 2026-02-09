package com.lensvault.feature.gallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lensvault.core.domain.repository.MediaRepository
import com.lensvault.core.model.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    val mediaPagingFlow: Flow<PagingData<Media>> = mediaRepository.getPagedMedia()
        .cachedIn(viewModelScope)
}
