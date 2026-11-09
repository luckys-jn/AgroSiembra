package com.angelramirez.agresiembre.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
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
import com.angelramirez.agresiembre.data.ParcelManager
import com.angelramirez.agresiembre.ui.theme.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToRecommendations: () -> Unit = {},
    onNavigateToParcels: () -> Unit = {},
    onNavigateToGuides: () -> Unit = {}
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val activeParcel = ParcelManager.selectedParcel

    // Mantiene el estado en la memoria global de la parcela seleccionada
    var latitud by remember(activeParcel.id) { mutableStateOf(activeParcel.latitude) }
    var longitud by remember(activeParcel.id) { mutableStateOf(activeParcel.longitude) }
    var altitud by remember(activeParcel.id) { mutableStateOf(activeParcel.altitude) }
    var climaCalculado by remember(activeParcel.id, altitud) {
        mutableStateOf(determinarClimaPorAltitud(altitud.toDoubleOrNull() ?: 0.0))
    }
    var gpsActivo by remember(activeParcel.id) { mutableStateOf(activeParcel.isGpsActive) }
    var cargandoGps by remember { mutableStateOf(false) }

    var areaDisponible by remember(activeParcel.id) { mutableStateOf(activeParcel.area) }
    var tempPromedio by remember { mutableStateOf("22") }

    var modoTecnicoActivo by remember { mutableStateOf(true) }
    var phSuelo by remember(activeParcel.id) { mutableFloatStateOf(activeParcel.ph.toFloatOrNull() ?: 6.5f) }
    var texturaSeleccionada by remember(activeParcel.id) { mutableStateOf(activeParcel.soilType) }

    // Función auxiliar para activar GPS
    val solicitarUbicacion = {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled) {
            Toast.makeText(context, "⚠️ Redirigiendo para encender el GPS...", Toast.LENGTH_SHORT).show()
            // Abre automáticamente el panel de encendido de Ubicación en el celular
            val intent = android.content.Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        } else {
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
                    climaCalculado = determinarClimaPorAltitud(altitud.toDoubleOrNull() ?: 0.0)

                    // Sincroniza globalmente con la parcela activa
                    ParcelManager.updateActiveParcelData(
                        altitude = altitud,
                        lat = latitud,
                        lng = longitud,
                        gpsActive = true
                    )
                    Toast.makeText(context, "📍 Ubicación detectada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se logró señal GPS. Intenta al aire libre.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            solicitarUbicacion()
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
                    onClick = onNavigateToParcels,
                    icon = { Icon(Icons.Default.GridView, contentDescription = null) },
                    label = { Text("Parcelas") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToGuides,
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
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GreenChipBg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToParcels() }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Parcela Activa:", fontSize = 10.sp, color = TextSecondary)
                                Text(activeParcel.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                            }
                            Icon(Icons.Default.SwapHoriz, contentDescription = "Cambiar", tint = GreenPrimary)
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
                        Text("Ubicación y Clima", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                        Surface(shape = RoundedCornerShape(8.dp), color = GreenChipBg) {
                            Text(if (gpsActivo) "Sincronizado" else "Manual", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 11.sp, color = GreenDark, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            if (hasPermission) {
                                solicitarUbicacion()
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
                            Text("Buscando GPS...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.GpsFixed, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Detectar mi ubicación actual (GPS)", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(shape = RoundedCornerShape(8.dp), color = GreenLightBg, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(if (latitud.isEmpty()) "📍 Coordenadas sin detectar" else "📍 Lat $latitud° N, Long $longitud° W", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Altitud: ${if (altitud.isEmpty()) "--" else altitud} msnm | Clima: $climaCalculado", fontSize = 12.sp, color = TextSecondary)
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

                    Text("Área del lote", fontSize = 13.sp, color = TextDark)
                    OutlinedTextField(
                        value = areaDisponible,
                        onValueChange = {
                            areaDisponible = it
                            ParcelManager.updateActiveParcelData(area = it)
                        },
                        placeholder = { Text("Ej: 2.5") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        suffix = { Text("ha") }
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
                                    ParcelManager.updateActiveParcelData(altitude = it)
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
                        Text("🧪 Modo Técnico", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                        Switch(checked = modoTecnicoActivo, onCheckedChange = { modoTecnicoActivo = it }, colors = SwitchDefaults.colors(checkedThumbColor = GreenPrimary))
                    }

                    AnimatedVisibility(visible = modoTecnicoActivo) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            Divider()
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("🌱 pH del Suelo", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("${String.format("%.1f", phSuelo)} pH", fontWeight = FontWeight.Bold, color = GreenPrimary)
                            }
                            Slider(
                                value = phSuelo,
                                onValueChange = {
                                    phSuelo = it
                                    ParcelManager.updateActiveParcelData(ph = String.format("%.1f", it))
                                },
                                valueRange = 4.0f..9.0f,
                                steps = 50,
                                colors = SliderDefaults.colors(thumbColor = GreenPrimary, activeTrackColor = GreenPrimary)
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("🌾 Tipo de Suelo", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Arcilloso", "Arenoso", "Limoso", "Franco").forEach { textura ->
                                    val selected = texturaSeleccionada == textura
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (selected) GreenPrimary else GreenChipBg,
                                        modifier = Modifier.weight(1f).clickable {
                                            texturaSeleccionada = textura
                                            ParcelManager.updateActiveParcelData(soilType = textura)
                                        }
                                    ) {
                                        Text(textura, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (selected) Color.White else TextDark, modifier = Modifier.padding(vertical = 8.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.TravelExplore, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Analizar Terreno y Ver Recomendaciones", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun obtenerUbicacionGPSReal(context: Context, fusedClient: com.google.android.gms.location.FusedLocationProviderClient, onResult: (Location?) -> Unit) {
    val cancelToken = CancellationTokenSource()
    fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancelToken.token).addOnSuccessListener { loc ->
        if (loc != null) onResult(loc)
        else fusedClient.lastLocation.addOnSuccessListener { lastLoc -> onResult(lastLoc) }.addOnFailureListener { onResult(null) }
    }.addOnFailureListener { onResult(null) }
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