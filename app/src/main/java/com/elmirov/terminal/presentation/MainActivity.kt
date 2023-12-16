package com.elmirov.terminal.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elmirov.terminal.presentation.TerminalScreenState.Content
import com.elmirov.terminal.presentation.TerminalScreenState.Initial
import com.elmirov.terminal.ui.theme.TerminalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TerminalTheme {
                val viewModel: TerminalViewModel = viewModel()
                val screenState = viewModel.state.collectAsState()

                when (val currentState = screenState.value) {
                    Initial -> Unit

                    is Content -> {
                        Terminal(bars = currentState.bars)
                    }
                }
            }
        }
    }
}
