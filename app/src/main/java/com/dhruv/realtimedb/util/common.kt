package com.dhruv.realtimedb.util

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog


@Composable
fun CommonDialog(modifier: Modifier = Modifier) {
    Dialog(onDismissRequest = {

    }) {
        CircularProgressIndicator()
    }
}