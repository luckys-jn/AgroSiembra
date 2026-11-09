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
import com.angelramirez.agresiembre.ui.theme.*
import kotlin.collections.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    altitudIngresada: Int = 1420,
    phIngresado: Float = 6.5f,
    onNavigateToHome: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Individuales, 1: Asociación

    // Catálogo/Biblioteca de cultivos con rangos agronómicos de altitud y pH
    val catalogCrops = remember {
        listOf(
            Crop("1", "Maíz Criollo Amarillo", "Zea mays • Variedad rústica nativa", 0, "3-4 meses", "Media (600mm)", "Suelo Franco", "Alta tolerancia a vientos. Excelente tutor vertical para asociaciones con leguminosas.", minAltitud = 0, maxAltitud = 2200, minPh = 5.5f, maxPh = 7.5f),
            Crop("2", "Frijol Arbustivo Rojo", "Phaseolus vulgaris • Crecimiento determinado", 0, "2.5 meses", "Baja-Media", "Fija Nitrógeno", "Aporta hasta 40kg N/ha al suelo. Ciclo biológico acelerado ideal para rotaciones.", minAltitud = 500, maxAltitud = 2500, minPh = 6.0f, maxPh = 7.5f),
            Crop("3", "Calabaza / Zapallo", "Cucurbita pepo • Rastrera tradicional", 0, "3 meses", "Media", "Cobertura viva", "Su follaje denso sombrea el suelo previniendo la evaporación hídrica y malezas.", minAltitud = 0, maxAltitud = 2000, minPh = 5.5f, maxPh = 7.0f),
            Crop("4", "Tomate Chonto", "Solanum lycopersicum • Semideterminado", 0, "3.5 meses", "Alta", "Suelo Franco", "Gran demanda local. Requiere tutorado e inspección de humedad constante.", minAltitud = 800, maxAltitud = 2000, minPh = 6.0f, maxPh = 6.8f),
            Crop("5", "Papa Sabanera", "Solanum tuberosum • Variedad de clima frío", 0, "5 meses", "Alta", "Suelo Suelto", "Cultivo rústico adaptado a tierras altas y temperaturas bajas.", minAltitud = 2000, maxAltitud = 3200, minPh = 5.0f, maxPh = 6.5f)
        )
    }

    // Filtrado automático según datos calibrados en la pantalla de Inicio
    val cultivosFiltrados = remember(altitudIngresada, phIngresado) {
        catalogCrops.filter { crop ->
            altitudIngresada in crop.minAltitud..crop.maxAltitud && phIngresado in crop.minPh..crop.maxPh
        }
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
                    Text("Biblioteca de Recomendaciones", fontSize = 12.sp, color = TextSecondary)
                }
                Surface(shape = RoundedCornerShape(50), color = GreenChipBg) {
                    Text("👤", modifier = Modifier.padding(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Chip Informativo del terreno calibrado
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
                    Text("Filtro: $altitudIngresada msnm | pH $phIngresado", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDark)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tabs Individual vs Policultivo
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = GreenChipBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    TabButton(
                        text = "Cultivos Aptos",
                        isSelected = selectedTab == 0,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = 0 }
                    )
                    TabButton(
                        text = "Asociación / Policultivo",
                        isSelected = selectedTab == 1,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedTab = 1 }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedTab == 0) {
                if (cultivosFiltrados.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay cultivos en la biblioteca para esa altitud/pH.", color = TextSecondary, fontSize = 13.sp)
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
                        AssociationSimulatorCard(onNavigateToDetail = onNavigateToDetail)
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
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) GreenPrimary else TextSecondary
            )
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(crop.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                    Text(crop.scientificName, fontSize = 11.sp, color = TextSecondary)
                }
            }

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
fun AssociationSimulatorCard(onNavigateToDetail: (String) -> Unit) {
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

            Text("Cultivos integrantes:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                GuildChip("🌽 Maíz\nSoporte/Tutor", modifier = Modifier.weight(1f))
                GuildChip("🫘 Frijol\nFijador de N", modifier = Modifier.weight(1f))
                GuildChip("🎃 Calabaza\nCobertura viva", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(shape = RoundedCornerShape(12.dp), color = GreenLightBg, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("💡 Relación Agronómica", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "El frijol fija nitrógeno que nutre al maíz, el maíz sirve de tutor natural, y las hojas de la calabaza cubren el suelo reteniendo humedad.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onNavigateToDetail("milpa") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.FormatListBulleted, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver plan de siembra combinada (Milpa)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
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
