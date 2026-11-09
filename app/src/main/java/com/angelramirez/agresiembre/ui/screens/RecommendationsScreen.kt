package com.angelramirez.agresiembre.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angelramirez.agresiembre.data.Crop
import com.angelramirez.agresiembre.data.ParcelManager
import com.angelramirez.agresiembre.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToParcels: () -> Unit = {},
    onNavigateToGuides: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Obtiene los datos de la Parcela Activa
    val activeParcel = ParcelManager.selectedParcel
    val altitudIngresada = activeParcel.altitude.toDoubleOrNull()?.toInt() ?: 0
    val phIngresado = activeParcel.ph.toFloatOrNull() ?: 6.5f
    val sueloIngresado = activeParcel.soilType

    // Catálogo con requerimientos agronómicos reales (Altitud, pH y Suelo)
    val catalogCrops = remember {
        listOf(
            Crop(
                id = "1",
                name = "Maíz Criollo Amarillo",
                scientificName = "Zea mays • Variedad rústica nativa",
                harvestTime = "3-4 meses",
                waterNeed = "Media (600mm)",
                soilSuitability = "Franco / Mixto",
                description = "Alta tolerancia a vientos. Excelente tutor vertical para asociaciones con leguminosas.",
                minAltitud = 0, maxAltitud = 2200, minPh = 5.5f, maxPh = 7.5f,
                compatibleSoils = listOf("Franco", "Mixto / Franco", "Limoso")
            ),
            Crop(
                id = "2",
                name = "Frijol Arbustivo Rojo",
                scientificName = "Phaseolus vulgaris • Crecimiento determinado",
                harvestTime = "2.5 meses",
                waterNeed = "Baja-Media",
                soilSuitability = "Fija Nitrógeno",
                description = "Aporta hasta 40kg N/ha al suelo. Ciclo biológico acelerado ideal para rotaciones.",
                minAltitud = 500, maxAltitud = 2500, minPh = 6.0f, maxPh = 7.5f,
                compatibleSoils = listOf("Franco", "Mixto / Franco", "Arenoso")
            ),
            Crop(
                id = "3",
                name = "Calabaza / Zapallo",
                scientificName = "Cucurbita pepo • Rastrera tradicional",
                harvestTime = "3 meses",
                waterNeed = "Media",
                soilSuitability = "Cobertura viva",
                description = "Su follaje denso sombrea el suelo previniendo la evaporación hídrica y malezas.",
                minAltitud = 0, maxAltitud = 2000, minPh = 5.5f, maxPh = 7.0f,
                compatibleSoils = listOf("Franco", "Mixto / Franco", "Arcilloso")
            ),
            Crop(
                id = "4",
                name = "Tomate Chonto",
                scientificName = "Solanum lycopersicum • Semideterminado",
                harvestTime = "3.5 meses",
                waterNeed = "Alta",
                soilSuitability = "Suelo Franco",
                description = "Gran demanda local. Requiere tutorado e inspección de humedad constante.",
                minAltitud = 800, maxAltitud = 2000, minPh = 6.0f, maxPh = 6.8f,
                compatibleSoils = listOf("Franco", "Mixto / Franco")
            ),
            Crop(
                id = "5",
                name = "Papa Sabanera",
                scientificName = "Solanum tuberosum • Variedad de clima frío",
                harvestTime = "5 meses",
                waterNeed = "Alta",
                soilSuitability = "Suelo Suelto",
                description = "Cultivo rústico adaptado a tierras altas y temperaturas bajas.",
                minAltitud = 2000, maxAltitud = 3200, minPh = 5.0f, maxPh = 6.5f,
                compatibleSoils = listOf("Arenoso", "Franco", "Mixto / Franco")
            )
        )
    }

    // Filtrado automático triple (Altitud + pH + Tipo de suelo)
    val cultivosFiltrados = remember(altitudIngresada, phIngresado, sueloIngresado) {
        if (altitudIngresada == 0) {
            catalogCrops
        } else {
            catalogCrops.filter { crop ->
                altitudIngresada in crop.minAltitud..crop.maxAltitud &&
                        phIngresado in crop.minPh..crop.maxPh &&
                        (sueloIngresado.isEmpty() || crop.compatibleSoils.contains(sueloIngresado))
            }
        }
    }

    // Evaluación del filtro para la Milpa (Maíz + Frijol + Calabaza)
    val milpaEsApta = remember(altitudIngresada, phIngresado) {
        if (altitudIngresada == 0) true
        else (altitudIngresada in 0..2200) && (phIngresado in 5.5f..7.5f)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHome,
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
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
                    Text("Recomendaciones", fontSize = 12.sp, color = TextSecondary)
                }
                Surface(shape = RoundedCornerShape(50), color = GreenChipBg) {
                    Text("👤", modifier = Modifier.padding(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chip Informativo de la Parcela Activa
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.FilterAlt, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Filtro [${activeParcel.name}]: ${if (altitudIngresada == 0) "Sin calibrar" else "$altitudIngresada msnm"} | pH $phIngresado | $sueloIngresado",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = TextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = GreenChipBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    TabButton(text = "Cultivos Aptos", isSelected = selectedTab == 0, modifier = Modifier.weight(1f), onClick = { selectedTab = 0 })
                    TabButton(text = "Asociación / Policultivo", isSelected = selectedTab == 1, modifier = Modifier.weight(1f), onClick = { selectedTab = 1 })
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedTab == 0) {
                if (cultivosFiltrados.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay cultivos aptos para esa combinación de altitud, pH y tipo de suelo.", color = TextSecondary, fontSize = 13.sp)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(cultivosFiltrados) { crop ->
                            CropCardItem(crop = crop, phActual = phIngresado, onNavigateToDetail = onNavigateToDetail)
                        }
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        AssociationSimulatorCard(
                            isApta = milpaEsApta,
                            altitudActual = altitudIngresada,
                            onNavigateToDetail = onNavigateToDetail
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color.White else Color.Transparent,
        modifier = modifier.clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 10.dp)) {
            Text(text = text, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) GreenPrimary else TextSecondary)
        }
    }
}

@Composable
fun CropCardItem(crop: Crop, phActual: Float, onNavigateToDetail: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(crop.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
            Text(crop.scientificName, fontSize = 11.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoBadge(title = "Cosecha", value = crop.harvestTime, modifier = Modifier.weight(1f))
                InfoBadge(title = "Agua", value = crop.waterNeed, modifier = Modifier.weight(1f))
                InfoBadge(title = "Suelo (pH)", value = "pH $phActual Óptimo", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(crop.description, fontSize = 12.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onNavigateToDetail(crop.id) },
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Ver guía completa", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun InfoBadge(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(8.dp), color = GreenChipBg, modifier = modifier) {
        Column(modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 10.sp, color = TextSecondary)
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextDark)
        }
    }
}

@Composable
fun AssociationSimulatorCard(
    isApta: Boolean,
    altitudActual: Int,
    onNavigateToDetail: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Groups, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Asociación Tradicional (Milpa)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark)
                    Text("Guía de policultivo ancestral", fontSize = 11.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                GuildChip("🌽 Maíz\nSoporte/Tutor", modifier = Modifier.weight(1f))
                GuildChip("🫘 Frijol\nFijador de N", modifier = Modifier.weight(1f))
                GuildChip("🎃 Calabaza\nCobertura viva", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Estado de aptitud agronómica de la Milpa
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isApta) GreenLightBg else Color(0xFFFFEBEE),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = if (isApta) "💡 Relación Agronómica Óptima" else "⚠️ Policultivo No Recomendado",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (isApta) TextDark else Color.Red
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isApta) {
                            "El frijol fija nitrógeno que nutre al maíz, el maíz sirve de tutor natural, y las hojas de la calabaza cubren el suelo reteniendo humedad."
                        } else {
                            "La Milpa requiere un clima cálido-templado (< 2200 msnm). Tu altitud actual ($altitudActual msnm) es muy elevada para el desarrollo del maíz y la calabaza."
                        },
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onNavigateToDetail("milpa") },
                enabled = isApta,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.FormatListBulleted, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isApta) "Ver plan de siembra combinada (Milpa)" else "No apto para esta altitud",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun GuildChip(text: String, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(10.dp), color = GreenChipBg, modifier = modifier) {
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(8.dp))
    }
}