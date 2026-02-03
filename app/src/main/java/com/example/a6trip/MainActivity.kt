package com.example.a6trip

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a6trip.ui.theme.Aqua80
import com.example.a6trip.ui.theme.GrayD
import com.example.a6trip.ui.theme.GrayL
import com.example.a6trip.ui.theme.GreenL
import com.example.a6trip.ui.theme._6tripTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _6tripTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginPage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPage(modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isChecked by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
        modifier = modifier
            .padding(16.dp, 185.dp)
            .fillMaxSize()
            .shadow(5.dp, RoundedCornerShape(25.dp))
            .border(2.dp, White, RoundedCornerShape(25.dp))
            .background(White, RoundedCornerShape(25.dp))
    ) {
        Text(
            text = "BEM-VINDO(A)",
            fontSize = 40.sp,
            color = GrayD,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.offset(0.dp, (-35).dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.width(315.dp).height(50.dp).offset(0.dp, (-8).dp).border(2.dp, GrayD, shape = RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(25.dp),
            placeholder = { Text("E-MAIL", fontStyle = FontStyle.Italic, color = GrayL, fontSize = 12.sp) }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(315.dp).height(50.dp).border(2.dp, GrayD, shape = RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(25.dp),
            placeholder = { Text("SENHA", fontStyle = FontStyle.Italic, color = GrayL, fontSize = 12.sp) }
        )

        Row(modifier = Modifier){
            Checkbox(

                checked = isChecked, // Uma variável booleana que controla o estado do checkbox
                onCheckedChange = { newValue ->
                    isChecked = newValue // Atualize o estado conforme necessário
                },
                modifier = Modifier.offset((-22).dp,12.dp), // Exemplo de modificador
                enabled = true,
                colors = CheckboxColors(
                    checkedCheckmarkColor = White,
                    uncheckedCheckmarkColor = Color(0xFF80D8FF), // Aqua80 (cor azul clara)
                    checkedBoxColor = GreenL, // Fundo verde quando marcado
                    uncheckedBoxColor = White, // Fundo branco quando desmarcado
                    disabledCheckedBoxColor =  GrayL ,
                    disabledUncheckedBoxColor = GrayL ,
                    disabledIndeterminateBoxColor = GrayL,
                    checkedBorderColor = GreenL,
                    uncheckedBorderColor = GrayD, // Borda cinza escura
                    disabledBorderColor = GrayL,
                    disabledUncheckedBorderColor = GrayL,
                    disabledIndeterminateBorderColor = GrayL
                )

            )

            Text(text = "Lembrar-me?",
                fontSize = 12.sp,
                color = GrayD,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                modifier = modifier.offset((-26).dp,26.dp)
            )
            Button(
                modifier = modifier.background(Color.Transparent,shape = RoundedCornerShape(25.dp)).offset(25.dp,14.dp),
                onClick = {

                    try {
                        /*
                        val intent = Intent(activity, TrocarSenhaActivity::class.java)
                        intent.flags = FLAG_ACTIVITY_SINGLE_TOP
                        activity?.startActivity(intent)
                        */

                    } catch (e: Exception) {
                        e.printStackTrace() // Captura exceções para depuração
                        Toast.makeText(activity, "Erro ao iniciar atividade", Toast.LENGTH_SHORT).show()
                    }
                } ,
                colors= ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = GrayD,
                    disabledContainerColor = GrayL,
                    disabledContentColor = GrayD,
                )
            ){ Text(text = "Deseja alterar senha?",
                fontSize = 12.sp,

                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
            ) }
        }

        Button(
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier.width(315.dp).offset(0.dp, 15.dp),
            onClick = {
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity!!) { task ->
                        if (task.isSuccessful) {
                            if (isChecked) {
                                salvarCredenciais(context, email, password)
                            } else {
                                apagarCredenciais(context)
                            }
                            /*
                            activity.startActivity(
                                Intent(activity, MainMenu::class.java).setFlags(
                                    FLAG_ACTIVITY_SINGLE_TOP
                                )
                            )
                            */

                            Toast.makeText(activity, "Login OK!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(activity, "Login FALHOU!", Toast.LENGTH_LONG).show()
                        }
                    }
            },
            colors = ButtonColors(
                containerColor = Aqua80,
                contentColor = GrayD,
                disabledContainerColor = GrayL,
                disabledContentColor = GrayD,
            )
        ) {
            Text("LOGIN", fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = {
                try {
                    /*
                    activity?.startActivity(
                        Intent(activity, CadastroActivity::class.java).setFlags(
                            FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_NO_HISTORY
                        )
                    )
                    */

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Erro ao iniciar atividade", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.width(315.dp).offset(0.dp, 15.dp),
            colors = ButtonColors(
                containerColor = Aqua80,
                contentColor = GrayD,
                disabledContainerColor = Aqua80,
                disabledContentColor = GrayD,
            )
        ) {
            Text("CADASTRAR", fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
        }
    }
}

private fun salvarCredenciais(context: Context, email: String, senha: String) {
    val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("email", email)
        putString("senha", senha)
        putBoolean("lembrar", true)
        apply()
    }
}

// Função para apagar as credenciais ao fazer logout
private fun apagarCredenciais(context: Context) {
    val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        clear()
        apply()
    }
}