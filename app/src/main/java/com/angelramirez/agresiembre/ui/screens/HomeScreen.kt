package com.angelramirez.agresiembre.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.angelramirez.agresiembre.ui.theme.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToRecommendations: () -> Unit = {}
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var altitud by remember { mutableStateOf("") }
    var climaCalculado by remember { mutableStateOf("Pendiente de calibrar") }
    var precisionGps by remember { mutableStateOf("Desconectado") }
    var gpsActivo by remember { mutableStateOf(false) }
    var cargandoGps by remember { mutableStateOf(false) }

    var areaDisponible by remember { mutableStateOf("") }
    var esHectareas by remember { mutableStateOf(false) }
    var tempPromedio by remember { mutableStateOf("") }

    var modoTecnicoActivo by remember { mutableStateOf(true) }
    var phSuelo by remember { mutableFloatStateOf(6.5f) }
    var texturaSeleccionada by remember { mutableStateOf("Mixto / Franco") }
    var humedadRelativa by remember { mutableStateOf("70%") }
    var drenajeSeleccionado by remember { mutableStateOf("Bueno") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cargandoGps = true
            obtenerUbicacionGPSReal(context, fusedLocationClient) { loc ->
                cargandoGps = false
                if (loc != null) {
                    latitud = String.format("%.4f", loc.latitude)
                    longitud = String.format("%.4f", loc.longitude)
                    if (loc.hasAltitude()) {
                        altitud = loc.altitude.roundToInt().toString()
                    }
                    gpsActivo = true
                    precisionGps = "±3 metros"
                    climaCalculado = determinarClimaPorAltitud(altitud.toDoubleOrNull() ?: 0.0)
                } else {
                    Toast.makeText(context, "Asegúrate de tener el GPS activo en tu teléfono", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToRecommendations,
                    icon = { Icon(Icons.Default.Recommend, contentDescription = null) },
                    label = { Text("Recom.") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.GridView, contentDescription = null) },
                    label = { Text("Parcelas") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Book, contentDescription = null) },
                    label = { Text("Guías") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GreenLightBg)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🌱 AgroSiembra", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextDark)
                Surface(shape = RoundedCornerShape(50), color = GreenChipBg) {
                    Text("👤", modifier = Modifier.padding(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("¡Hola, Agricultor!", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                    Text("Listo para calibrar tu siembra", fontSize = 13.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MyLocation, contentDescription = null, tint = GreenPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ubicación y Clima", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                        }
                        Surface(shape = RoundedCornerShape(8.dp), color = GreenChipBg) {
                            Text(
                                text = if (gpsActivo) "Sincronizado" else "Manual",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = GreenDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasPermission) {
                                cargandoGps = true
                                obtenerUbicacionGPSReal(context, fusedLocationClient) { loc ->
                                    cargandoGps = false
                                    if (loc != null) {
                                        latitud = String.format("%.4f", loc.latitude)
                                        longitud = String.format("%.4f", loc.longitude)
                                        if (loc.hasAltitude()) {
                                            altitud = loc.altitude.roundToInt().toString()
                                        }
                                        gpsActivo = true
                                        precisionGps = "±3 metros"
                                        climaCalculado = determinarClimaPorAltitud(altitud.toDoubleOrNull() ?: 0.0)
                                    } else {
                                        Toast.makeText(context, "Activa la Ubicación/GPS de tu teléfono e intenta nuevamente", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        },
                        enabled = !cargandoGps,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (cargandoGps) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Obteniendo señal GPS...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.GpsFixed, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Detectar mi ubicación actual (GPS)", fontWeight = FontWeight.Bold)
                                Text("Precisión estimada: $precisionGps", fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GreenLightBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (latitud.isEmpty()) "📍 Coordenadas sin detectar" else "📍 Lat $latitud° N, Long $longitud° W",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Altitud: ${if (altitud.isEmpty()) "--" else altitud} msnm | Clima: $climaCalculado",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📐 Dimensiones y Clima Base", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Área del lote disponible", fontSize = 13.sp, color = TextDark)
                    OutlinedTextField(
                        value = areaDisponible,
                        onValueChange = { areaDisponible = it },
                        placeholder = { Text("Ej: 2.5") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        suffix = { Text(if (esHectareas) "ha" else "m²") }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Altitud sobre mar", fontSize = 12.sp, color = TextSecondary)
                            OutlinedTextField(
                                value = altitud,
                                onValueChange = {
                                    altitud = it
                                    climaCalculado = determinarClimaPorAltitud(it.toDoubleOrNull() ?: 0.0)
                                },
                                placeholder = { Text("Ej: 1420") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(10.dp),
                                suffix = { Text("msnm") }
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Temp. promedio", fontSize = 12.sp, color = TextSecondary)
                            OutlinedTextField(
                                value = tempPromedio,
                                onValueChange = { tempPromedio = it },
                                placeholder = { Text("Ej: 22") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(10.dp),
                                suffix = { Text("°C") }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("🧪 Modo Técnico / Avanzado", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                            Text("Calibración edafológica del suelo", fontSize = 12.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = modoTecnicoActivo,
                            onCheckedChange = { modoTecnicoActivo = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = GreenPrimary)
                        )
                    }

                    AnimatedVisibility(visible = modoTecnicoActivo) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            Divider()
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("🌱 pH del Suelo", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("${String.format("%.1f", phSuelo)} pH", fontWeight = FontWeight.Bold, color = GreenPrimary)
                            }

                            Slider(
                                value = phSuelo,
                                onValueChange = { phSuelo = it },
                                valueRange = 4.0f..9.0f,
                                steps = 50,
                                colors = SliderDefaults.colors(thumbColor = GreenPrimary, activeTrackColor = GreenPrimary)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("🌾 Textura y Tipo de Suelo", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Arcilloso", "Arenoso", "Limoso", "Mixto / Franco").forEach { textura ->
                                    val selected = texturaSeleccionada == textura
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (selected) GreenPrimary else GreenChipBg,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { texturaSeleccionada = textura }
                                    ) {
                                        Text(
                                            text = textura,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) Color.White else TextDark,
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateToRecommendations,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.TravelExplore, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Analizar Terreno y Ver Recomendaciones", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun obtenerUbicacionGPSReal(
    context: Context,
    fusedClient: com.google.android.gms.location.FusedLocationProviderClient,
    onResult: (Location?) -> Unit
) {
    val cancelToken = CancellationTokenSource()

    // 1. Intenta pedir la ubicación actual exacta en tiempo real encendiendo los sensores
    fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancelToken.token)
        .addOnSuccessListener { location ->
            if (location != null) {
                onResult(location)
            } else {
                // 2. Si es nulo, usa la última ubicación guardada como respaldo
                fusedClient.lastLocation.addOnSuccessListener { lastLoc ->
                    onResult(lastLoc)
                }.addOnFailureListener {
                    onResult(null)
                }
            }
        }
        .addOnFailureListener {
            onResult(null)
        }
}

private fun determinarClimaPorAltitud(msnm: Double): String {
    if (msnm == 0.0) return "Sin calibrar"
    return when {
        msnm < 1000 -> "Cálido (0 - 1000 msnm)"
        msnm in 1000.0..2000.0 -> "Templado húmedo (1000 - 2000 msnm)"
        msnm in 2000.0..3000.0 -> "Frío (2000 - 3000 msnm)"
        else -> "Páramo (> 3000 msnm)"
    }
}
