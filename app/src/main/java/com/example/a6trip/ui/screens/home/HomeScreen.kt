package com.example.a6trip.ui.screens.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CameraAlt


import androidx.compose.ui.Alignment.Companion.CenterHorizontally

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.a6trip.data.auth.AuthRepository
import com.example.a6trip.data.auth.CloudinaryClient
import com.example.a6trip.ui.components.Logo6Trip
import com.example.a6trip.ui.model.Place
import com.example.a6trip.ui.model.User
import com.example.a6trip.ui.theme.Black
import com.example.a6trip.ui.theme.BorderLight
import com.example.a6trip.ui.theme.Red
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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
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
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.forEach

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
    val context = LocalContext.current
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Você pode tratar aqui se quiser
    }

    LaunchedEffect(Unit) {
        authRepository.getCurrentUserProfile { result ->
            isLoading = false
            result.onSuccess { userProfile = it }
            result.onFailure { loadError = it.message }
            val permissionCheckCamera = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            )

            if (permissionCheckCamera != PackageManager.PERMISSION_GRANTED) {
                cameraPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
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
                                MapPage(modifier = Modifier)
                            }

                            "Inventário" -> {
                               // Text("Tela de Inventário")
                                InventarioTuristicoMenu(modifier = Modifier)
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
fun InventarioTuristicoMenu(modifier: Modifier = Modifier){
    //var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var namePlaces by remember { mutableStateOf("") }
    var placeFind by remember { mutableStateOf(false) }
    var showAll by remember { mutableStateOf(false) }
    var places by remember { mutableStateOf<List<Place>>(emptyList()) }

    val context = LocalContext.current
    val authRepository = remember {
        AuthRepository(context = context)
    }
    LaunchedEffect(Unit) {
        authRepository.getPlaces { result ->
            result.onSuccess {
                places = it
            }
        }
    }
    if (places.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nenhum local cadastrado ainda.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
        }
        return
    }

    if(!placeFind && !showAll){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // 👈 Espaço entre as Rows
            ) {

                // 🔎 Linha de busca
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = namePlaces,
                        onValueChange = { namePlaces = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(responsiveButtonHeight()),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        placeholder = {
                            Text(
                                "Nome do Lugar",
                                fontStyle = FontStyle.Italic,
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    )

                    Button(
                        onClick = { placeFind = true },
                        modifier = Modifier
                            .height(responsiveButtonHeight()),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Black,
                            contentColor = White
                        ),
                        enabled = namePlaces.isNotBlank()
                    ) {
                        Text("Buscar")
                    }
                }

                // 📋 Botão exibir todos
                Button(
                    onClick = { showAll = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(responsiveButtonHeight()),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Black,
                        contentColor = White
                    )
                ) {
                    Text("Exibir todos")
                }
            }
        }


    }else if(showAll && !placeFind){
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {

            items(places) { place ->

                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        // Título
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)){
                            Button(
                                onClick = {
                                    placeFind = false
                                    showAll = false
                                },
                                modifier = Modifier.padding(vertical = 0.dp)
                            ){
                                Text("X")
                            }
                            Text(
                                text = place.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Black,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }


                        Spacer(modifier = Modifier.height(4.dp))

                        // Categoria
                        Text(
                            text = place.categoria,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Imagem
                        AsyncImage(
                            model = place.imageUrl,
                            contentDescription = place.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Descrição
                        Text(
                            text = place.descricao,
                            fontSize = 14.sp,
                            color = TextSecondary,
                            lineHeight = 20.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Justify
                        )
                    }

                }
            }
        }
    }else if(!showAll && placeFind){
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {

            items(places) { place ->

                if(place.name.toUpperCase() == namePlaces.toUpperCase()){
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        modifier = Modifier.fillMaxWidth(),
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            // Título
                            Row(modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)){
                                Button(
                                    onClick = {
                                        placeFind = false
                                        showAll = false
                                    },
                                    modifier = Modifier.padding(vertical = 0.dp)
                                ){
                                    Text("X")
                                }
                                Text(
                                    text = place.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Black,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Categoria
                            Text(
                                text = place.categoria,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Imagem
                            AsyncImage(
                                model = place.imageUrl,
                                contentDescription = place.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Descrição
                            Text(
                                text = place.descricao,
                                fontSize = 14.sp,
                                color = TextSecondary,
                                lineHeight = 20.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Justify
                            )
                        }

                    }
                }

            }
        }
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
                        onClick = {
                            onCategoryClick(label)
                            Log.d("DRAWER", "Selecionado: $label")
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
fun showPhoto(authRepository: AuthRepository) {

    var places by remember { mutableStateOf<List<Place>>(emptyList()) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }

    LaunchedEffect(Unit) {
        authRepository.getPlaces { result ->
            result.onSuccess { places = it }
        }
    }

    // Cria os marcadores
    places.forEach { place ->
        Marker(
            state = MarkerState(position = LatLng(place.lat, place.longi)),
            title = place.name,
            snippet = place.categoria,
            onClick = {
                selectedPlace = place
                false
            }
        )
    }

    // Dialog separado
    selectedPlace?.let { place ->

        AlertDialog(
            onDismissRequest = { selectedPlace = null },
            confirmButton = {},
            title = { Text(place.name) },
            text = {
                Column {
                    AsyncImage(
                        model = place.imageUrl,   // 👈 usa direto
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(place.descricao)
                }
            }
        )
    }
}
@Composable
fun MapPage(modifier: Modifier = Modifier) {

    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    val authRepository = remember {
        AuthRepository(context = context)
    }
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if(hasLocationPermission){

        GoogleMap(
            modifier = modifier.fillMaxSize(),
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)
        ) {
            showPhoto(authRepository)
            MapEffect { map ->
                map.setOnMyLocationButtonClickListener {
                    map.myLocation?.let {
                        latitude = it.latitude
                        longitude = it.longitude
                        showMenu = true
                    }
                    false
                }
            }
        }
    } else {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ative sua localização para interagir com o mapa.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
        }
    }

    if (showMenu) {
        MyLocationMenu(
            latitude = latitude,
            longitude = longitude,
            onDismiss = { showMenu = false }
        )
    }
}

// Lógica de Processamento de Imagem e Upload
suspend fun saveAndUploadToCloudinary(
    context: Context,
    bitmap: Bitmap,
    latitude: Double,
    longitude: Double
): String? {
    return withContext(Dispatchers.IO) {
        try {
            // Nome do arquivo baseado nas coordenadas conforme solicitado
            val fileName = "lat_${latitude}_long_${longitude}.jpg"
            val file = File(context.cacheDir, fileName)

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }

            val requestFile = file.readBytes().toRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val preset = "ml_default".toRequestBody("text/plain".toMediaType())

            val response = CloudinaryClient.api.uploadImage("duectt1dt", body, preset)

            if (response.isSuccessful) {
                Log.d("CLOUDINARY", "URL: ${response.body()?.secure_url}")
                response.body()?.secure_url
            } else {
                Log.e("CLOUDINARY", "Erro: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("CLOUDINARY", "Exception: ", e)
            null
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLocationMenu(latitude: Double, longitude: Double, onDismiss: () -> Unit) {

    val context = LocalContext.current

    val authRepository = remember {
        AuthRepository(context = context)
    }
    var nomeLocal by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Alimentação") }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val categorias = listOf("Alimentação", "Cultura", "Compras", "Turismo", "Serviços", "Religioso", "Histórico", "Natureza")

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        capturedBitmap = bitmap
    }
    val hasCameraPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
    Column(
        modifier = Modifier
            .padding(6.dp)
            .background(color = White, shape = RoundedCornerShape(15.dp))
            .border(3.dp, color = Color.Transparent, RoundedCornerShape(12.dp))
            .padding(24.dp)
            .fillMaxHeight(0.9f)
            .verticalScroll(rememberScrollState())
    ) {
        if(hasCameraPermission){
            Row(modifier = Modifier.fillMaxWidth()){
                IconButton(onClick = onDismiss) {
                    Text("X", fontWeight = FontWeight.Bold, color = Black)
                }
                Text("Adicionar estabelecimento",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 14.dp, start = 27.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Lat: $latitude / Long: $longitude", fontSize = 12.sp, color = TextSecondary)

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = nomeLocal,
                onValueChange = { nomeLocal = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Nome do local", fontStyle = FontStyle.Italic, fontSize = 14.sp) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Black, cursorColor = Black)
            )

            Spacer(modifier = Modifier.height(12.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                TextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor().border(1.dp, color = BorderLight, shape = RoundedCornerShape(16.dp)),
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categorias.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { selectedOption = option; expanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = descricao,
                onValueChange = { if (it.length <= 500) descricao = it },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Descrição", fontStyle = FontStyle.Italic, fontSize = 14.sp) },
                trailingIcon = {
                    Text("${descricao.length}/500", fontSize = 10.sp, modifier = Modifier.padding(top = 100.dp, end = 10.dp))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão da Câmera
            Button(
                onClick = { cameraLauncher.launch() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (capturedBitmap == null) Black else Color(0xFF4CAF50)
                )
            ) {
                Icon(imageVector = Icons.Filled.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (capturedBitmap == null) "Tirar Foto" else "Foto Capturada!")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Confirmar
            val fileName = "lat_${latitude}_long_${longitude}.jpg"
            Button(
                onClick = {
                    capturedBitmap?.let { bitmap ->
                        isUploading = true
                        scope.launch {
/*
                            authRepository.savePlaceData(fileName, nomeLocal, selectedOption, descricao, latitude, longitude){ result ->
                                result.onSuccess {
                                    Toast.makeText(context, "Dados Salvos.", Toast.LENGTH_LONG).show()

                                }.onFailure {
                                    Toast.makeText(context, "Erro: ${it.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                            val url = saveAndUploadToCloudinary(context, bitmap, latitude, longitude)
                            isUploading = false
                            if (url != null) onDismiss()

 */
                            val url = saveAndUploadToCloudinary(context, bitmap, latitude, longitude)

                            if (url != null) {

                                authRepository.savePlaceData(
                                    fileName,
                                    nomeLocal,
                                    selectedOption,
                                    descricao,
                                    latitude,
                                    longitude,
                                    url   // 👈 passa a URL real
                                ) { result ->
                                    result.onSuccess {
                                        Toast.makeText(context, "Dados Salvos.", Toast.LENGTH_LONG).show()
                                        onDismiss()
                                    }.onFailure {
                                        Toast.makeText(context, "Erro: ${it.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }

                            isUploading = false
                        }
                    }
                },
                enabled = capturedBitmap != null && nomeLocal.isNotBlank() && !isUploading,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Black)
            ) {
                if (isUploading) CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                else Text("Confirmar Registro", fontWeight = FontWeight.SemiBold)
            }
        }else{
            Text("Ative sua câmera para adicionar um novo estabelecimento.",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 14.dp, start = 27.dp))
        }

    }
}

@Composable
private fun BottomBranding(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = White) {
    }
}