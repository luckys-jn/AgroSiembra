package com.angelramirez.agresiembre.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.angelramirez.agresiembre.data.*
import com.angelramirez.agresiembre.ui.theme.*
import com.angelramirez.agresiembre.util.PdfGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropDetailScreen(
    cropId: String = "1",
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Siembra, 1: Requerimientos, 2: Plagas, 3: Manejo

    // Carga de datos dinámicos y únicos según el ID seleccionado
    val crop = remember(cropId) { CropRepository.getCropDetail(cropId) }

    val tabTitles = listOf("1. Siembra", "2. Requerimientos", "3. Plagas", "4. Manejo")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🌱 Ficha De Guía", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    Text("👤", modifier = Modifier.padding(end = 16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenLightBg)
            )
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
            // Tarjeta Cabecera con datos dinámicos del cultivo
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GreenDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(shape = RoundedCornerShape(12.dp), color = Color(0x33FFFFFF)) {
                        Text("🌿 Variedad Tradicional", color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(crop.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(crop.scientificName, fontSize = 12.sp, color = Color.LightGray)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HeaderMetric("Ciclo Total", crop.cycleDays, modifier = Modifier.weight(1f))
                        HeaderMetric("Riego", crop.waterRequirement, modifier = Modifier.weight(1f))
                        HeaderMetric("pH Suelo", crop.phRange, modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector Horizontal de Pestañas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tabTitles.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) GreenPrimary else Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = index }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(
                                text = title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido según la pestaña activa
            when (selectedTab) {
                0 -> SiembraTabContent(crop)
                1 -> RequerimientosTabContent(crop)
                2 -> PlagasTabContent(crop)
                3 -> ManejoOrganicoTabContent()
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón PDF Real con contenido específico
            Button(
                onClick = {
                    PdfGenerator.generateCropPdf(context, crop, tabTitles[selectedTab])
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CloudDownload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Descargar Guía en PDF (Offline)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun HeaderMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(10.dp), color = GreenChipBg, modifier = modifier) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, color = TextSecondary)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
        }
    }
}

@Composable
fun SiembraTabContent(crop: CropDetail) {
    Column {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🌱 Paso a Paso de Campo", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                Spacer(modifier = Modifier.height(12.dp))
                crop.steps.forEach { step ->
                    Row(modifier = Modifier.padding(vertical = 6.dp)) {
                        Surface(shape = CircleShape, color = GreenPrimary, modifier = Modifier.size(24.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("${step.stepNumber}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(step.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                            Text(step.description, fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📅 Calendario de Etapas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                Spacer(modifier = Modifier.height(12.dp))
                crop.calendar.forEach { cal ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("• ${cal.stage}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDark)
                            Text(cal.description, fontSize = 11.sp, color = TextSecondary)
                        }
                        Text(cal.days, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = GreenPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun RequerimientosTabContent(crop: CropDetail) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🌡️ Factores Críticos de Rendimiento", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
            Spacer(modifier = Modifier.height(12.dp))
            crop.requirements.forEach { req ->
                Surface(shape = RoundedCornerShape(12.dp), color = GreenChipBg, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(req.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                        Text(req.subtitle, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = GreenDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(req.description, fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun PlagasTabContent(crop: CropDetail) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🐛 Monitoreo de Plagas y Hongos", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
            Spacer(modifier = Modifier.height(12.dp))
            crop.pests.forEach { pest ->
                Surface(shape = RoundedCornerShape(12.dp), color = GreenChipBg, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(pest.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                            Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFEBEE)) {
                                Text(pest.riskLevel, color = Color.Red, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
                            }
                        }
                        Text(pest.scientificName, fontSize = 11.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(pest.description, fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun ManejoOrganicoTabContent() {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🛡️ Manejo Agronómico Verde (Orgánico)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
            Spacer(modifier = Modifier.height(12.dp))
            Text("• Control Biológico: Liberación de avispitas Trichogramma o predadores naturales.", fontSize = 12.sp, color = TextDark)
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Preparados Botánicos: Extracto de nim al 2% o purín de ortiga como biocida foliar preventivo.", fontSize = 12.sp, color = TextDark)
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Biofertilización: Aplicación de biol de estiércol enriquecido con ceniza cada 15 días.", fontSize = 12.sp, color = TextDark)
        }
    }
}
