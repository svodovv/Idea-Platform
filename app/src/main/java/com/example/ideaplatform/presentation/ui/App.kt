package com.example.ideaplatform.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ideaplatform.presentation.ui.screens.ItemViewModel

@Composable
fun App(
    viewModel: ItemViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        state.value.itemList?.forEach {
            Text(text = it.name)
        }
    }
}
