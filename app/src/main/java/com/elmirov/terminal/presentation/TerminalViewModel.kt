package com.elmirov.terminal.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmirov.terminal.data.network.ApiFactory
import com.elmirov.terminal.presentation.TerminalScreenState.Content
import com.elmirov.terminal.presentation.TerminalScreenState.Initial
import com.elmirov.terminal.presentation.TerminalScreenState.Loading
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TerminalViewModel : ViewModel() {

    private val apiService = ApiFactory.apiService

    private val _state = MutableStateFlow<TerminalScreenState>(Initial)
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("TerminalViewModel", "Exception: $throwable")
    }

    init {
        getBars()
    }

    private fun getBars() {
        _state.value = Loading

        viewModelScope.launch(exceptionHandler) {
            val bars = apiService.getBars().bars
            _state.value = Content(bars = bars)
        }
    }
}