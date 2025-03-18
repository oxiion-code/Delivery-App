package com.oxiion.campusmen.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingDialog(isLoading: MutableState<Boolean>) {
    if (isLoading.value) {
        Dialog(
            onDismissRequest = {}
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF29638A),
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}