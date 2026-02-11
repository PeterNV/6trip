package com.example.a6trip.ui.screens.welcome

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a6trip.ui.components.Logo6Trip
import com.example.a6trip.ui.components.TopBarPlaceholder
import com.example.a6trip.ui.theme.Black
import com.example.a6trip.ui.theme.White
import com.example.a6trip.ui.theme.responsiveHorizontalPadding
import com.example.a6trip.ui.theme.responsiveLogoSizeLarge
import com.example.a6trip.ui.theme.responsiveSpacerLarge
import com.example.a6trip.ui.theme.responsiveSpacerMedium
import com.example.a6trip.ui.theme.responsiveSpacerSmall
import com.example.a6trip.ui.theme.responsiveButtonHeight

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "welcome_alpha"
    )

    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onNavigateToHome()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .alpha(alpha),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBarPlaceholder()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = responsiveHorizontalPadding())
                .padding(vertical = responsiveSpacerMedium()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Logo6Trip(size = responsiveLogoSizeLarge())
            Spacer(modifier = Modifier.height(responsiveSpacerSmall()))
            Text(
                text = "6Trip",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
            Spacer(modifier = Modifier.height(responsiveSpacerLarge()))
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .widthIn(max = 320.dp)
                    .height(responsiveButtonHeight()),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Black,
                    contentColor = White
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = "Entrar",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
