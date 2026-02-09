package com.lensvault.feature.security.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor() : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    fun onAuthenticationSuccess() {
        _isAuthenticated.value = true
    }

    fun onAuthenticationFailed() {
        _isAuthenticated.value = false
    }
    
    fun onLock() {
        _isAuthenticated.value = false
    }
}
