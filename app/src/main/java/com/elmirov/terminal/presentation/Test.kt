package com.elmirov.terminal.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Test() {
    var testData by rememberSaveable(saver = TestData.saver) {
        mutableStateOf(TestData(0))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { testData = testData.copy(number = testData.number + 1) },
        contentAlignment = Alignment.Center,

        ) {
        Text(text = "Text: $testData")
    }
}

data class TestData(
    val number: Int,
) {
    companion object {
        val saver: Saver<MutableState<TestData>, Int> = Saver(
            save = {
                it.value.number
            },
            restore = {
                mutableStateOf(TestData(number = it))
            },
        )
    }
}