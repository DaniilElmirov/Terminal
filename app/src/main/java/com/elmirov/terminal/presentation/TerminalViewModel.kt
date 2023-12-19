package com.elmirov.terminal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmirov.terminal.data.network.KtorClient
import com.elmirov.terminal.presentation.TerminalScreenState.Content
import com.elmirov.terminal.presentation.TerminalScreenState.Initial
import com.elmirov.terminal.presentation.TerminalScreenState.Loading
import com.elmirov.terminal.presentation.TimeFrame.HOUR_1
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TerminalViewModel : ViewModel() {

    private val apiService = KtorClient

    private val _state = MutableStateFlow<TerminalScreenState>(Initial)
    val state = _state.asStateFlow()

    private var lastState: TerminalScreenState = Initial

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _state.value = lastState //Бесплатно 5 запросов, если больше падает ошибка
    }

    init {
        getBars()
    }

    fun getBars(timeFrame: TimeFrame = HOUR_1) {
        lastState = _state.value

        _state.value = Loading

        viewModelScope.launch(exceptionHandler) {
            val bars = apiService.getBars(timeFrame = timeFrame.value).bars
            _state.value = Content(bars = bars, timeFrame = timeFrame)
        }
    }
}