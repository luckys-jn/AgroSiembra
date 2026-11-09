package com.angelramirez.agresiembre.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.angelramirez.agresiembre.data.CropDetail
import com.angelramirez.agresiembre.data.ParcelManager
import java.io.File
import java.io.FileOutputStream

object PdfGenerator {

    fun generateCropPdf(context: Context, crop: CropDetail, currentTabTitle: String) {
        val activeParcel = ParcelManager.selectedParcel
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        // Encabezado
        paint.color = Color.parseColor("#1B6B2B")
        canvas.drawRect(0f, 0f, 595f, 100f, paint)

        paint.color = Color.WHITE
        paint.textSize = 22f
        paint.isFakeBoldText = true
        canvas.drawText("AgroSiembra - ${activeParcel.name}", 40f, 50f, paint)

        paint.textSize = 13f
        paint.isFakeBoldText = false
        canvas.drawText("Guía Offline: ${crop.name}", 40f, 80f, paint)

        paint.color = Color.BLACK
        paint.textSize = 15f
        paint.isFakeBoldText = true
        canvas.drawText("Cultivo: ${crop.name} (${crop.scientificName})", 40f, 140f, paint)

        paint.textSize = 12f
        paint.isFakeBoldText = false
        canvas.drawText("• Ciclo Total: ${crop.cycleDays}  |  • Riego: ${crop.waterRequirement}  |  • pH: ${crop.phRange}", 40f, 165f, paint)
        canvas.drawText("• Sección: $currentTabTitle", 40f, 185f, paint)

        paint.color = Color.GRAY
        canvas.drawLine(40f, 205f, 555f, 205f, paint)

        var yPos = 240f
        paint.color = Color.BLACK
        paint.textSize = 14f
        paint.isFakeBoldText = true
        canvas.drawText("Resumen de Labores:", 40f, yPos, paint)

        yPos += 30f
        paint.textSize = 11f
        paint.isFakeBoldText = false

        crop.steps.forEach { step ->
            if (yPos < 780f) {
                canvas.drawText("${step.stepNumber}. ${step.title}:", 40f, yPos, paint)
                yPos += 18f
                canvas.drawText("   ${step.description.take(80)}...", 40f, yPos, paint)
                yPos += 25f
            }
        }

        paint.color = Color.parseColor("#1B6B2B")
        paint.textSize = 10f
        canvas.drawText("Generado para ${activeParcel.name} - Guardado en Descargas", 40f, 810f, paint)

        pdfDocument.finishPage(page)

        // Nombre del PDF prefijado con el ID de la Parcela activa
        val fileName = "AgroSiembra_Parcela${activeParcel.id}_${crop.name.replace(" ", "_")}.pdf"

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                }
            } else {
                val targetFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                pdfDocument.writeTo(FileOutputStream(targetFile))
            }
            Toast.makeText(context, "📄 PDF guardado para ${activeParcel.name}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error al guardar el PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }
}