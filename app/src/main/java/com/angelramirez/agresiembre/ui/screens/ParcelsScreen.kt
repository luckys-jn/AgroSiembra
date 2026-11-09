package com.angelramirez.agresiembre.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angelramirez.agresiembre.data.Parcel
import com.angelramirez.agresiembre.data.ParcelManager
import com.angelramirez.agresiembre.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToRecommendations: () -> Unit = {},
    onNavigateToGuides: () -> Unit = {}
) {
    val context = LocalContext.current
    val parcelsList = ParcelManager.parcels

    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = GreenPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.AddLocation, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("+ Agregar Parcela", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHome,
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
                    selected = true,
                    onClick = { },
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("🌱 AgroSiembra", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                    Text("Gestión De Parcelas", fontSize = 12.sp, color = TextSecondary)
                }
                Surface(shape = RoundedCornerShape(50), color = GreenChipBg) {
                    Text("👤", modifier = Modifier.padding(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Parcelas", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Supervisión y administración de lotes agrícolas", fontSize = 12.sp, color = TextSecondary)

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Assessment, contentDescription = null, tint = GreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Panorama de Producción", fontWeight = FontWeight.Bold, color = TextDark, fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Lotes Registrados", fontSize = 11.sp, color = TextSecondary)
                            Text("${parcelsList.size} Lote(s)", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                        }
                        Column {
                            Text("Lote Activo actual", fontSize = 11.sp, color = TextSecondary)
                            Text(ParcelManager.selectedParcel.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GreenPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(parcelsList) { index, parcel ->
                    val isCurrent = parcel.id == ParcelManager.selectedParcel.id
                    ParcelItemCard(
                        index = index + 1,
                        parcel = parcel,
                        isSelected = isCurrent,
                        onSelect = {
                            ParcelManager.selectParcel(parcel)
                            Toast.makeText(context, "Slot activo: '${parcel.name}'", Toast.LENGTH_SHORT).show()
                            onNavigateToHome()
                        }
                    )
                }
            }
        }
    }

    // Modal Crear Parcela (Sólo pide Nombre)
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("🌱 Agregar Nueva Parcela", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nombre de la parcela / Lote") },
                        placeholder = { Text("Ej: Lote 2 - La Esperanza") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank()) {
                            ParcelManager.addParcel(newName.trim())
                            Toast.makeText(context, "¡Parcela creada! Selecciónala para calibrar.", Toast.LENGTH_SHORT).show()
                            newName = ""
                            showAddDialog = false
                        } else {
                            Toast.makeText(context, "Ingresa un nombre válido", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Crear Parcela")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun ParcelItemCard(index: Int, parcel: Parcel, isSelected: Boolean, onSelect: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(50), color = GreenPrimary, modifier = Modifier.size(28.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("$index", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(parcel.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                }
                if (isSelected) {
                    Surface(shape = RoundedCornerShape(8.dp), color = GreenChipBg) {
                        Text("Activo", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GreenDark, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ParcelMetricBadge(label = "Área", value = parcel.area.ifEmpty { "Sin definir" }, modifier = Modifier.weight(1f))
                ParcelMetricBadge(label = "Altitud", value = if (parcel.altitude.isEmpty()) "Sin definir" else "${parcel.altitude} msnm", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ParcelMetricBadge(label = "pH Suelo", value = parcel.ph.ifEmpty { "6.5" }, modifier = Modifier.weight(1f))
                ParcelMetricBadge(label = "Tipo de Suelo", value = parcel.soilType.ifEmpty { "Franco" }, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier.fillMaxWidth().height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) GreenPrimary else GreenChipBg),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null, tint = if (isSelected) Color.White else GreenDark, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isSelected) "Slot en uso (Ver Terreno)" else "Ver terreno", color = if (isSelected) Color.White else GreenDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = if (isSelected) Color.White else GreenDark, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun ParcelMetricBadge(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = GreenLightBg,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(label, fontSize = 10.sp, color = TextSecondary)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
        }
    }
}