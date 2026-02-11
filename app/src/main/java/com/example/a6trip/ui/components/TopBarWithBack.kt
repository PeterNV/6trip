package com.example.a6trip.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.a6trip.ui.theme.Black
import com.example.a6trip.ui.theme.White

private val TopBarHeight = 56.dp

@Composable
fun TopBarWithBack(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Voltar"
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = White,
        shadowElevation = 0.dp
    ) {
        Box(modifier = Modifier.height(TopBarHeight)) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = contentDescription,
                    tint = Black
                )
            }
        }
    }
}

@Composable
fun TopBarPlaceholder(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = White,
        shadowElevation = 0.dp
    ) {
        Box(modifier = Modifier.height(TopBarHeight))
    }
}
