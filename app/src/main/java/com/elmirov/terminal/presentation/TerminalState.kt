package com.elmirov.terminal.presentation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.elmirov.terminal.data.model.Bar
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class TerminalState(
    val bars: List<Bar>,
    val terminalWidth: Float = 1f,
    val terminalHeight: Float = 1f,
    val visibleBarCount: Int = 100,
    val scrollBy: Float = 0f,
) : Parcelable {
    val barWidth: Float
        get() = terminalWidth / visibleBarCount

    private val visibleBars: List<Bar>
        get() {
            val startIndex = (scrollBy / barWidth).roundToInt().coerceAtLeast(0)
            val endIndex = (startIndex + visibleBarCount).coerceAtMost(bars.size)
            return bars.subList(startIndex, endIndex)
        }

    val min: Float
        get() = visibleBars.minOf { it.low }
    val max: Float
        get() = visibleBars.maxOf { it.high }
    val pxPerPoint: Float
        get() = terminalHeight / (max - min)
}

@Composable
fun rememberTerminalState(bars: List<Bar>): MutableState<TerminalState> {
    return rememberSaveable {
        mutableStateOf(TerminalState(bars = bars))
    }
}

