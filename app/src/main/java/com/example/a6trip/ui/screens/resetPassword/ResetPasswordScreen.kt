package com.example.a6trip.ui.screens.resetPassword

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a6trip.data.auth.AuthRepository
import com.example.a6trip.ui.components.Logo6Trip
import com.example.a6trip.ui.components.TopBarWithBack
import com.example.a6trip.ui.theme.Black
import com.example.a6trip.ui.theme.BorderLight
import com.example.a6trip.ui.theme.SurfaceLight
import com.example.a6trip.ui.theme.TextSecondary
import com.example.a6trip.ui.theme.White
import com.example.a6trip.ui.theme.responsiveButtonHeight
import com.example.a6trip.ui.theme.responsiveContentMaxWidth
import com.example.a6trip.ui.theme.responsiveHorizontalPadding
import com.example.a6trip.ui.theme.responsiveLogoSizeSmall
import com.example.a6trip.ui.theme.responsiveSpacerMedium
import com.example.a6trip.ui.theme.responsiveSpacerSmall

@Composable
fun ResetPasswordScreen(
    authRepository: AuthRepository,
    onBack: () -> Unit,
    onResetPasswordSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        TopBarWithBack(onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = responsiveHorizontalPadding()),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = responsiveContentMaxWidth())
                        .fillMaxWidth()
                        .padding(vertical = responsiveSpacerSmall()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Logo6Trip(size = responsiveLogoSizeSmall())
                    Spacer(modifier = Modifier.height(responsiveSpacerSmall()))
                    Text(
                        text = "Troca de senha",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Spacer(modifier = Modifier.height(responsiveSpacerMedium()))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (email.isEmpty()) BorderLight
                                else if (email.contains("outlook") || email.contains("hotmail")) BorderLight
                                else Black,
                                RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = {
                            Text("E-mail", fontStyle = FontStyle.Italic, color = TextSecondary, fontSize = 14.sp)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Black,
                            unfocusedBorderColor = BorderLight,
                            focusedTextColor = Black,
                            unfocusedTextColor = Black,
                            cursorColor = Black,
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(modifier = Modifier.height(responsiveSpacerMedium()))
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth().height(responsiveButtonHeight()),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Black),
                        border = BorderStroke(1.dp, Black)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (email.isBlank()) {
                                Toast.makeText(context, "Preencha o e-mail.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            authRepository.resetPassword(email) { result ->
                                result.onSuccess {
                                    Toast.makeText(context, "E-mail enviado com sucesso.", Toast.LENGTH_LONG).show()
                                    onResetPasswordSuccess()
                                }.onFailure {
                                    Toast.makeText(context, "Erro: ${it.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        enabled = email.isNotEmpty() && !email.contains("hotmail") && !email.contains("outlook"),
                        modifier = Modifier.fillMaxWidth().height(responsiveButtonHeight()),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Black,
                            contentColor = White,
                            disabledContainerColor = TextSecondary.copy(alpha = 0.4f),
                            disabledContentColor = White
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("Confirmar", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
