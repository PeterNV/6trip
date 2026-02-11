package com.example.a6trip.ui.screens.register

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.colman.simplecpfvalidator.isCpf
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
fun RegisterScreen(
    authRepository: AuthRepository,
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var cpf by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var cpassword by rememberSaveable { mutableStateOf("") }
    var isCheckedPassword by rememberSaveable { mutableStateOf(false) }
    var isCheckedCPassword by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val regexPassword = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")

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
                        text = "Cadastro",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Spacer(modifier = Modifier.height(responsiveSpacerMedium()))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = {
                            Text("Nome", fontStyle = FontStyle.Italic, color = TextSecondary, fontSize = 14.sp)
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Spacer(modifier = Modifier.height(responsiveSpacerSmall()))
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
                    Spacer(modifier = Modifier.height(responsiveSpacerSmall()))
                    OutlinedTextField(
                        value = cpf,
                        onValueChange = { cpf = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (cpf.isEmpty()) BorderLight else if (!cpf.isCpf()) BorderLight else Black,
                                RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = {
                            Text("CPF", fontStyle = FontStyle.Italic, color = TextSecondary, fontSize = 14.sp)
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(responsiveSpacerSmall()))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (password.isEmpty()) BorderLight
                                else if (!regexPassword.containsMatchIn(password)) BorderLight
                                else Black,
                                RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = {
                            Text("Senha", fontStyle = FontStyle.Italic, color = TextSecondary, fontSize = 14.sp)
                        },
                        visualTransformation = if (!isCheckedPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            val image = if (isCheckedPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { isCheckedPassword = !isCheckedPassword }) {
                                Icon(imageVector = image, contentDescription = if (isCheckedPassword) "Ocultar senha" else "Mostrar senha", tint = Black)
                            }
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Spacer(modifier = Modifier.height(responsiveSpacerSmall()))
                    OutlinedTextField(
                        value = cpassword,
                        onValueChange = { cpassword = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (cpassword.isEmpty()) BorderLight else if (cpassword != password) BorderLight else Black,
                                RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = {
                            Text("Confirmar senha", fontStyle = FontStyle.Italic, color = TextSecondary, fontSize = 14.sp)
                        },
                        visualTransformation = if (!isCheckedCPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            IconButton(onClick = { isCheckedCPassword = !isCheckedCPassword }) {
                                Icon(
                                    imageVector = if (isCheckedCPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (isCheckedCPassword) "Ocultar senha" else "Mostrar senha",
                                    tint = Black
                                )
                            }
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mínimo 8 caracteres, letras maiúsculas, minúsculas e números.",
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        color = TextSecondary
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
                            if (email.isBlank() || password.isBlank() || name.isBlank() || cpf.isBlank()) {
                                Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            authRepository.signUp(email, password, name, cpf) { result ->
                                result.onSuccess {
                                    Toast.makeText(context, "E-mail enviado com sucesso.", Toast.LENGTH_LONG).show()
                                    onRegisterSuccess()
                                }.onFailure {
                                    Toast.makeText(context, "Erro: ${it.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        enabled = email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() &&
                                cpf.isNotEmpty() && password == cpassword &&
                                !email.contains("hotmail") && !email.contains("outlook") &&
                                regexPassword.containsMatchIn(password) && cpf.isCpf(),
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
