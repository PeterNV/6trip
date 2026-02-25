package com.example.a6trip.ui.screens.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CameraAlt


import androidx.compose.ui.Alignment.Companion.CenterHorizontally

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.a6trip.data.auth.AuthRepository
import com.example.a6trip.data.auth.CloudinaryClient
import com.example.a6trip.ui.components.Logo6Trip
import com.example.a6trip.ui.model.User
import com.example.a6trip.ui.theme.Black
import com.example.a6trip.ui.theme.BorderLight
import com.example.a6trip.ui.theme.SurfaceLight
import com.example.a6trip.ui.theme.TextPrimary
import com.example.a6trip.ui.theme.TextSecondary
import com.example.a6trip.ui.theme.White
import com.example.a6trip.ui.theme.responsiveButtonHeight
import com.example.a6trip.ui.theme.responsiveLogoSizeSmall
import com.example.a6trip.ui.theme.responsiveSpacerSmall
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val DrawerWidth = 280.dp
private val TopBarHeight = 56.dp

@Composable
fun HomeScreen(
    authRepository: AuthRepository,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var userProfile by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var selectedScreen by remember { mutableStateOf("Início") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        authRepository.getCurrentUserProfile { result ->
            isLoading = false
            result.onSuccess { userProfile = it }
            result.onFailure { loadError = it.message }
        }

    }

    ModalNavigationDrawer(
        modifier = modifier.fillMaxSize(),
        drawerState = drawerState,
        gesturesEnabled = selectedScreen != "Mapa",
        drawerContent = {
            DrawerContent(
                onCategoryClick = { label ->
                    selectedScreen = label
                    scope.launch { drawerState.close() }
                },
                onSettingsClick = { scope.launch { drawerState.close() } }
            )
        }
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceLight)
        ) {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(TopBarHeight)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = onSignOut) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair",
                            tint = Black
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 0.dp, start = 4.dp, end = 24.dp, bottom = 24.dp)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = CenterHorizontally) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = Black,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.height(responsiveSpacerSmall()))
                                Text(
                                    text = "Carregando...",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    loadError != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = CenterHorizontally) {
                                Text(
                                    text = "Não foi possível carregar seu perfil.",
                                    fontSize = 16.sp,
                                    color = TextPrimary,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = loadError ?: "",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    else -> {
                        when (selectedScreen) {

                            "Início" -> {
                                val displayName = userProfile?.name?.takeIf { it.isNotBlank() }
                                    ?: authRepository.currentUser?.email?.substringBefore('@')
                                    ?: "Usuário"

                                Row(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Logo6Trip(size = responsiveLogoSizeSmall())
                                    Text(
                                        text = "Olá, $displayName!",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Black
                                    )
                                }
                            }

                            "Mapa" -> {
                                MapPage(
                                    modifier = Modifier
                                )
                            }

                            "Inventário" -> {
                                Text("Tela de Inventário")
                            }
                        }



                    }
                }
            }
        }
        BottomBranding()

    }
}


@Composable
private fun DrawerContent(
    onCategoryClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("Início", "Mapa", "Inventário")

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(DrawerWidth)
            .background(White)
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        categories.forEach { label ->
            Text(
                text = label,
                fontSize = 16.sp,
                color = Black,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp, horizontal = 12.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onCategoryClick(label)
                            Log.d(toString(), "Selecionado:$label")

                        }
                    )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onSettingsClick
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Configurações",
                tint = Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Configurações",
                fontSize = 16.sp,
                color = Black,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MapPage(
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    val hasLocationPermission =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    if(hasLocationPermission){
        Text("")
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true
            )
        ) {

            // ✅ AGORA está no lugar correto
            MapEffect { map ->

                map.setOnMyLocationButtonClickListener {

                    val location = map.myLocation
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude
                        showMenu = true
                    }

                    false
                }
            }
        }
    }else{
        Text("Ative sua localização para adicionar um novo estabelecimento, e visualizar diferentes lugares em tempo real.",
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 14.dp, start = 27.dp))
    }


    if (showMenu) {
        MyLocationMenu(
            latitude = latitude,
            longitude = longitude
        )
    }

}
/*
suspend fun uploadToCloudinary(
    context: Context,
    imageUri: Uri
): String? {

    return try {

        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes() ?: return null

        val requestFile =
            //bytes.toRequestBody("image/#".toMediaTypeOrNull())

        val body = MultipartBody.Part.createFormData(
            "file",
            "image.jpg",
            requestFile
        )

        val preset =
            "ml_default".toRequestBody("text/plain".toMediaType())

        val response = CloudinaryClient.api.uploadImage(
            "duectt1dt",
            body,
            preset
        )

        if (response.isSuccessful) {
            Log.d("CLOUDINARY", "Sucesso: ${response.body()?.secure_url}")
            response.body()?.secure_url
        } else {
            Log.e("CLOUDINARY", "Erro: ${response.errorBody()?.string()}")
            null
        }

    } catch (e: Exception) {
        Log.e("CLOUDINARY", "Exception:", e)
        null
    }
}
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLocationMenu(latitude: Double,
                   longitude: Double){
    var showMap by remember { mutableStateOf(false) }
    var nomeLocal by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Alimentação") }
    val categorias = listOf("Alimentação", "Cultura", "Compras", "Turismo", "Serviços", "Religioso", "Histórico", "Natureza")
    Column(
        modifier = Modifier
            .padding(6.dp)
            .background(color = White, shape = RoundedCornerShape(15.dp)
            )
            .border(3.dp, color = Color.Transparent, RoundedCornerShape(12.dp))
            .padding(48.dp).height(675.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()){
            Button(
                onClick = {
                    showMap = true
                },

                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Black,
                    contentColor = White,
                    disabledContainerColor = TextSecondary.copy(alpha = 0.4f),
                    disabledContentColor = White
                ),
            ) {
                Text("X")
            }
            Text("Adicionar estabelecimento",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 14.dp, start = 27.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Latitude: $latitude")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Longitude: $longitude")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = nomeLocal,
            onValueChange = { nomeLocal = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    "Nome do estabelecimento",
                    fontStyle = FontStyle.Italic,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
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
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Categoria",
            fontStyle = FontStyle.Italic,
            fontSize = 14.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {

            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .border(1.dp, color = BorderLight, shape = RoundedCornerShape(16.dp)),

            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categorias.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = Black) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = descricao,
            onValueChange = {
                val digitsOnly = it.filter { char -> char.isDefined() }
                if (digitsOnly.length <= 500) {
                    descricao = digitsOnly
                }
            },
            modifier = Modifier.fillMaxWidth().height(175.dp),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    "Descrição",
                    fontStyle = FontStyle.Italic,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
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

            trailingIcon = {
                Text("${descricao.count()}/500",
                    color = BorderLight,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 128.dp).padding(end = 20.dp)
                )
            },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(12.dp))
        /*
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
        }

         */
        Button(
            onClick = {
                //launcher.launch("image/*")
            },

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
            Icon(
                imageVector = Icons.Filled.CameraAlt, // Use a default Material icon
                contentDescription = "Camera" // Provide an accessibility label
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        //val scope = rememberCoroutineScope()

        Button(
            onClick = {
                /*
                imageUri?.let { uri ->
                    scope.launch {
                        val url = uploadToCloudinary(context, uri)
                    }
                }

                 */
            },

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
            Text("Confirmar",fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
    if(showMap){
        MapPage(
            modifier = Modifier
        )
    }
}

@Composable
private fun BottomBranding(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = White,
        shadowElevation = 0.dp
    ) {
        /*
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "6Trip",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
                Text(
                    text = "Inventário Turístico",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

         */
    }
}
