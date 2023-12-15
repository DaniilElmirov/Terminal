package com.elmirov.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import com.elmirov.terminal.data.model.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BAR_COUNT = 20

@Composable
fun Terminal(
    bars: List<Bar>,
) {

    var visibleBarCount by remember {
        mutableStateOf(100)
    }

    var scrollBy by remember {
        mutableStateOf(0f)
    }

    var barWidth by remember {
        mutableStateOf(0f)
    }

    var terminalWidth by remember {
        mutableStateOf(0f)
    }

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        visibleBarCount = (visibleBarCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BAR_COUNT, bars.size)

        scrollBy = (scrollBy + panChange.x)
            .coerceIn(0f, bars.size * barWidth - terminalWidth)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .transformable(transformableState),
    ) {
        barWidth = size.width / visibleBarCount
        terminalWidth = size.width

        val min = bars.minOf { it.low }
        val max = bars.maxOf { it.high }
        val pxPerPoint = size.height / (max - min)

        translate(left = scrollBy) {
            bars.forEachIndexed { index, bar ->
                val offsetX = size.width - index * barWidth

                drawLine(
                    color = Color.White,
                    start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.high - min) * pxPerPoint)),
                    strokeWidth = 1f,
                )

                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(offsetX, size.height - ((bar.open - min) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.close - min) * pxPerPoint)),
                    strokeWidth = barWidth / 2,
                )
            }
        }
    }
}