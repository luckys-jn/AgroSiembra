package com.angelramirez.agresiembre.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.angelramirez.agresiembre.data.ParcelManager
import com.angelramirez.agresiembre.ui.theme.*
import java.io.File

data class SavedPdfItem(val fileName: String, val fileSize: String, val file: File)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedGuidesScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToRecommendations: () -> Unit = {},
    onNavigateToParcels: () -> Unit = {}
) {
    val context = LocalContext.current
    val activeParcel = ParcelManager.selectedParcel
    var savedPdfs by remember(activeParcel.id) { mutableStateOf(getSavedPdfsForParcel(context, activeParcel.id)) }

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
                    selected = true,
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
                    Text("Guías de ${activeParcel.name}", fontSize = 12.sp, color = TextSecondary)
                }
                Surface(shape = RoundedCornerShape(50), color = GreenChipBg) {
                    Text("👤", modifier = Modifier.padding(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FolderZip, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Biblioteca Offline: ${activeParcel.name}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextDark)
                        Text("Archivos PDF pertenecientes exclusivamente a esta parcela.", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (savedPdfs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No hay guías para ${activeParcel.name}.", color = TextSecondary, fontSize = 13.sp)
                        Text("Descarga guías mientras tengas esta parcela activa.", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(savedPdfs) { item ->
                        PdfCardItem(
                            item = item,
                            onOpen = { openPdf(context, item.file) },
                            onDelete = {
                                if (item.file.delete()) {
                                    savedPdfs = getSavedPdfsForParcel(context, activeParcel.id)
                                    Toast.makeText(context, "Archivo eliminado", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PdfCardItem(item: SavedPdfItem, onOpen: () -> Unit, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(shape = RoundedCornerShape(8.dp), color = GreenChipBg) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = GreenPrimary, modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(item.fileName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                    Text("Tamaño: ${item.fileSize}", fontSize = 11.sp, color = TextSecondary)
                }
            }
            Row {
                IconButton(onClick = onOpen) {
                    Icon(Icons.Default.Visibility, contentDescription = "Abrir", tint = GreenPrimary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}

private fun getSavedPdfsForParcel(context: Context, parcelId: String): List<SavedPdfItem> {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val prefix = "AgroSiembra_Parcela${parcelId}_"
    val pdfFiles = downloadsDir.listFiles { file ->
        file.isFile && file.name.startsWith(prefix) && file.name.endsWith(".pdf")
    } ?: arrayOf()

    return pdfFiles.map { file ->
        val kb = file.length() / 1024
        SavedPdfItem(
            fileName = file.name.replace(prefix, "").replace(".pdf", "").replace("_", " "),
            fileSize = "$kb KB",
            file = file
        )
    }
}

private fun openPdf(context: Context, file: File) {
    try {
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No hay visor de PDF instalado en este dispositivo", Toast.LENGTH_SHORT).show()
    }
}