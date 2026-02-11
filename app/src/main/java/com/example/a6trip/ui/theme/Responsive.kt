package com.example.a6trip.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun responsiveHorizontalPadding(): Dp {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    return when {
        screenWidthDp < 360 -> 16.dp
        screenWidthDp < 600 -> 24.dp
        else -> (screenWidthDp * 0.06f).dp.coerceIn(24.dp, 48.dp)
    }
}

@Composable
fun responsiveContentMaxWidth(): Dp {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    return when {
        screenWidthDp < 600 -> 9999.dp
        else -> 440.dp
    }
}

@Composable
fun responsiveLogoSizeSmall(): Dp {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    return (screenWidthDp * 0.22f).dp.coerceIn(80.dp, 120.dp)
}

@Composable
fun responsiveLogoSizeLarge(): Dp {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    return (screenWidthDp * 0.5f).dp.coerceIn(160.dp, 260.dp)
}

@Composable
fun responsiveSpacerSmall(): Dp = 16.dp

@Composable
fun responsiveSpacerMedium(): Dp = 24.dp

@Composable
fun responsiveSpacerLarge(): Dp {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    return if (screenHeightDp < 640) 24.dp else 32.dp
}

@Composable
fun responsiveButtonHeight(): Dp = 52.dp
