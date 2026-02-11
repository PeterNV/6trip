package com.example.a6trip.ui.screens.register


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import br.com.colman.simplecpfvalidator.isCpf
import com.example.a6trip.data.auth.AuthRepository
import com.example.a6trip.ui.components.Logo6Trip
import com.example.a6trip.ui.theme.Black
import com.example.a6trip.ui.theme.BorderLight
import com.example.a6trip.ui.theme.GreenL
import com.example.a6trip.ui.theme.Red
import com.example.a6trip.ui.theme.SurfaceLight
import com.example.a6trip.ui.theme.TextSecondary
import com.example.a6trip.ui.theme.White

@Composable
fun RegisterScreen(
    authRepository: AuthRepository,
    onBack: () -> Unit,
    onResgisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {

    var name by rememberSaveable { mutableStateOf("") }
    var cpf by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var cpassword by rememberSaveable { mutableStateOf("") }
    var isCheckedPasswrod by rememberSaveable { mutableStateOf(false) }
    var isCheckedCPasswrod by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo6Trip(size = 100.dp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Cadastro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text(
                    "Nome",
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .border(2.dp,  color = if(email.isEmpty()) Color.Transparent else if(email.contains("outlook") || email.contains("hotmail")) Red else GreenL, shape = RoundedCornerShape(20.dp)),
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
            value = cpf,
            onValueChange = { cpf = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .border(2.dp,  color = if(cpf.isEmpty()) Color.Transparent else if(!cpf.isCpf()) Red else GreenL, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text(
                    "Cpf",
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))
        val regexPassword = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(2.dp,  color = if(password.isEmpty()) Color.Transparent else if(!password.contains(regexPassword)) Red else GreenL, shape = RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                placeholder = {
                    Text(
                        "Senha",
                        fontStyle = FontStyle.Italic,
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                },
                visualTransformation = if(!isCheckedPasswrod) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = { // Add the eye icon as a trailing icon
                    val image = if (isCheckedPasswrod)
                        Icons.Filled.Visibility // Open eye icon
                    else
                        Icons.Filled.VisibilityOff // Closed/crossed-out eye icon

                    // Localized description for accessibility
                    val description = if (isCheckedPasswrod) "Hide password" else "Show password"

                    IconButton(onClick = { isCheckedPasswrod = !isCheckedPasswrod }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )



        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = cpassword,
            onValueChange = { cpassword = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .border(2.dp,  color = if(cpassword.isEmpty()) Color.Transparent else if(cpassword != password) Red else GreenL, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text(
                    "Confirmar senha",
                    fontStyle = FontStyle.Italic,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            visualTransformation = if(!isCheckedCPasswrod) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = { // Add the eye icon as a trailing icon
                val image = if (isCheckedCPasswrod)
                    Icons.Filled.Visibility // Open eye icon
                else
                    Icons.Filled.VisibilityOff // Closed/crossed-out eye icon

                // Localized description for accessibility
                val description = if (isCheckedCPasswrod) "Hide password" else "Show password"

                IconButton(onClick = { isCheckedCPasswrod = !isCheckedCPasswrod }) {
                    Icon(imageVector = image, contentDescription = description)
                }
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("A senha deve conter no mínimo 8 carcteres,\nincluindo letras maiúsculas e minúsculas, e \nnúmeros.",

            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Black,
            modifier = modifier.offset((0).dp,8.dp))

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                onBack()
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Red,
                contentColor = White,
                disabledContainerColor = Red,
                disabledContentColor = White
            )
        ) {
            Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank() || name.isBlank() || cpf.isBlank()) {
                    Toast.makeText(context, "Preencha nome, cpf, e-mail e senha para cadastrar.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                authRepository.signUp(email, password, name, cpf) { result ->
                    result.onSuccess {
                        Toast.makeText(context, "E-mail enviado com sucesso.", Toast.LENGTH_LONG).show()
                        onResgisterSuccess()
                    }.onFailure {
                        Toast.makeText(context, "Erro ao enviar e-mail: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            enabled = email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()
                    && cpf.isNotEmpty() && (password == cpassword)
                    && !email.contains("hotmail") && !email.contains("outlook")
                    && password.contains(regexPassword) && cpf.isCpf(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenL,
                contentColor = White,
                disabledContainerColor = GreenL,
                disabledContentColor = White
            )
        ) {
            Text("Confirmar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}