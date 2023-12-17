package com.elmirov.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elmirov.terminal.data.model.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BAR_COUNT = 20

@OptIn(ExperimentalTextApi::class)
@Composable
fun Terminal(
    bars: List<Bar>,
) {

    var terminalState by rememberTerminalState(bars = bars)

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        val visibleBarCount = (terminalState.visibleBarCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BAR_COUNT, bars.size)

        val scrollBy = (terminalState.scrollBy + panChange.x)
            .coerceIn(0f, bars.size * terminalState.barWidth - terminalState.terminalWidth)

        terminalState = terminalState.copy(visibleBarCount = visibleBarCount, scrollBy = scrollBy)
    }

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(
                top = 32.dp,
                bottom = 32.dp,
            )
            .transformable(transformableState)
            .onSizeChanged {
                terminalState = terminalState.copy(terminalWidth = it.width.toFloat())
            },
    ) {

        val min = terminalState.visibleBars.minOf { it.low }
        val max = terminalState.visibleBars.maxOf { it.high }
        val pxPerPoint = size.height / (max - min)

        translate(left = terminalState.scrollBy) {
            bars.forEachIndexed { index, bar ->
                val offsetX = size.width - index * terminalState.barWidth

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
                    strokeWidth = terminalState.barWidth / 2,
                )
            }
        }

        bars.firstOrNull()?.let {
            drawPricesLine(
                min = min,
                max = max,
                pxPerPoint = pxPerPoint,
                lastPrice = it.close,
                textMeasurer = textMeasurer,
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawPricesLine(
    min: Float,
    max: Float,
    pxPerPoint: Float,
    lastPrice: Float,
    textMeasurer: TextMeasurer,
) {
    //max
    drawDashLine(
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f),
    )
    drawPrice(
        textMeasurer = textMeasurer,
        price = max,
        offsetY = 0f,
    )

    //last price
    val lastPriceOffsetY = size.height - ((lastPrice - min) * pxPerPoint)
    drawDashLine(
        start = Offset(0f, lastPriceOffsetY),
        end = Offset(size.width, lastPriceOffsetY),
    )
    drawPrice(
        textMeasurer = textMeasurer,
        price = lastPrice,
        offsetY = lastPriceOffsetY,
    )

    //min
    drawDashLine(
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
    )
    drawPrice(
        textMeasurer = textMeasurer,
        price = min,
        offsetY = size.height,
    )
}

private fun DrawScope.drawDashLine(
    color: Color = Color.White,
    start: Offset,
    end: Offset,
    strokeWidth: Float = 1f,
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx())
        ),
    )
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawPrice(
    textMeasurer: TextMeasurer,
    price: Float,
    offsetY: Float,
) {
    val textLayoutResult = textMeasurer.measure(
        text = price.toString(),
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp,
        ),
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(size.width - textLayoutResult.size.width, offsetY),
    )
}