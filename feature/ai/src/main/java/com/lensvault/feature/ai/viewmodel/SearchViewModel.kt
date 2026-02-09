package com.lensvault.feature.ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lensvault.core.domain.repository.AiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Long>>(emptyList())
    val searchResults: StateFlow<List<Long>> = _searchResults

    fun onSearchQueryChanged(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        
        viewModelScope.launch {
            _searchResults.value = aiRepository.search(query)
        }
    }
}
