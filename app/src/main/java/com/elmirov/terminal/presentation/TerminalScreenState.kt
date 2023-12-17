package com.elmirov.terminal.presentation

import com.elmirov.terminal.data.model.Bar

sealed interface TerminalScreenState {

    object Initial : TerminalScreenState

    object Loading : TerminalScreenState

    data class Content(
        val bars: List<Bar>,
        val timeFrame: TimeFrame,
    ) : TerminalScreenState
}