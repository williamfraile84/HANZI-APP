package com.jukhg10.hanzimemo.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * 🤖 SERVICIO OPTIMIZADO: Encapsula el motor de Machine Learning offline (Google ML Kit OCR)
 * para el reconocimiento de caracteres chinos simplificados y tradicionales.
 */
class HanziRecognitionService(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    // Instancia única del reconocedor configurado específicamente para el alfabeto script Chino
    private val recognizer = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

    /**
     * 🚀 PROCESAMIENTO OCR ASÍNCRONO SEGURO:
     * Toma el URI de una imagen (recortada o capturada), extrae el texto de forma síncronaizada
     * dentro del hilo secundario de IO usando corrutinas, liberando el hilo principal por completo.
     *
     * @param imageUri URI local del archivo de imagen.
     * @return El string completo de caracteres detectados en la imagen, o null si ocurre un fallo.
     */
    suspend fun recognizeTextFromUri(imageUri: Uri): String? = withContext(ioDispatcher) {
        var bitmap: Bitmap? = null
        try {
            Log.d("HanziMemo.OCR", "Iniciando OCR para URI: $imageUri (scheme: ${imageUri.scheme})")
            val inputImage = try {
                InputImage.fromFilePath(context, imageUri)
            } catch (e: Exception) {
                Log.w("HanziMemo.OCR", "InputImage.fromFilePath falló, intentando decodificación manual: ${e.message}")
                bitmap = when {
                    imageUri.scheme == "file" && imageUri.path != null -> {
                        android.graphics.BitmapFactory.decodeFile(imageUri.path)
                    }
                    else -> {
                        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                            android.graphics.BitmapFactory.decodeStream(inputStream)
                        }
                    }
                }
                if (bitmap != null) {
                    InputImage.fromBitmap(bitmap, 0)
                } else {
                    throw IOException("No se pudo decodificar el bitmap desde $imageUri")
                }
            }

            val visionText = Tasks.await(recognizer.process(inputImage))
            val text = visionText.text
            Log.d("HanziMemo.OCR", "OCR completado. Caracteres detectados: '$text'")
            return@withContext text
        } catch (e: Exception) {
            Log.e("HanziMemo.OCR", "Fallo durante el proceso OCR: ${e.message}", e)
            return@withContext null
        } finally {
            try {
                bitmap?.recycle()
            } catch (_: Exception) {}
        }
    }

    /**
     * 🚀 PROCESAMIENTO DIRECTO DESDE BITMAP:
     * Útil si se procesa directamente el stream de frames de CameraX sin persistir archivos en disco.
     */
    suspend fun recognizeTextFromBitmap(bitmap: Bitmap): String? = withContext(ioDispatcher) {
        try {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val visionText = Tasks.await(recognizer.process(inputImage))
            return@withContext visionText.text
        } catch (e: Exception) {
            Log.e("HanziMemo.OCR", "Fallo en recognizeTextFromBitmap: ${e.message}", e)
            return@withContext null
        }
        // Nota: No reciclamos el bitmap aquí porque su ciclo de vida pertenece al invocador (ej. CameraX lifecycle)
    }

    /**
     * 🔒 LIBERACIÓN DE MODELOS:
     * Debe llamarse en el onCleared del ViewModel correspondiente o en el ciclo de vida del contenedor
     * para destruir los hilos internos del cliente nativo de Google C++ y descargar el modelo offline de la RAM.
     */
    fun close() {
        try {
            recognizer.close()
            Log.d("HanziMemo.OCR", "Motor de reconocimiento de texto cerrado correctamente.")
        } catch (e: Exception) {
            Log.e("HanziMemo.OCR", "Error al cerrar el reconocedor de texto: ${e.message}", e)
        }
    }
}