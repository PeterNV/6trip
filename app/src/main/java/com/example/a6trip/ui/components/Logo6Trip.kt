package com.example.a6trip.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.a6trip.R

@Composable
fun Logo6Trip(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    Image(
        painter = painterResource(id = R.drawable.logo_6trip),
        contentDescription = "Logo 6Trip",
        modifier = modifier.size(size)
    )
}
