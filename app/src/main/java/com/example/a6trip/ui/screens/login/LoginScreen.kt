package com.example.a6trip.ui.screens.login

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a6trip.data.auth.AuthRepository
import com.example.a6trip.ui.components.Logo6Trip
import com.example.a6trip.ui.theme.Black
import com.example.a6trip.ui.theme.BorderLight
import com.example.a6trip.ui.theme.SurfaceLight
import com.example.a6trip.ui.theme.TextSecondary
import com.example.a6trip.ui.theme.White

@Composable
fun LoginScreen(
    authRepository: AuthRepository,
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isChecked by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        authRepository.getSavedCredentials()?.let { cred ->
            email = cred.email
            password = cred.senha
            isChecked = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.offset((-8).dp, (-8).dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_revert),
                    contentDescription = "Voltar"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Logo6Trip(size = 100.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Bem-vindo(a)",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text(
                    "E-mail",
                    fontStyle = FontStyle.Italic,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Black,
                unfocusedBorderColor = BorderLight,
                focusedTextColor = Black,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = Black,
                focusedContainerColor = White,
                unfocusedContainerColor = White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text(
                    "Senha",
                    fontStyle = FontStyle.Italic,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Black,
                unfocusedBorderColor = BorderLight,
                focusedTextColor = Black,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = Black,
                focusedContainerColor = White,
                unfocusedContainerColor = White
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Black,
                    uncheckedColor = BorderLight
                )
            )
            Text(
                text = "Lembrar-me",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = "Alterar senha?",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                authRepository.signIn(email, password) { result ->
                    result.onSuccess {
                        if (isChecked) authRepository.saveCredentials(email, password)
                        else authRepository.clearCredentials()
                        Toast.makeText(context, "Login realizado.", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    }.onFailure {
                        Toast.makeText(context, "Login falhou: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Black,
                contentColor = White,
                disabledContainerColor = TextSecondary.copy(alpha = 0.5f),
                disabledContentColor = White
            )
        ) {
            Text("Entrar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = {
                onRegister()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Black),
            border = BorderStroke(1.dp, Black)
        ) {
            Text("Cadastrar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
