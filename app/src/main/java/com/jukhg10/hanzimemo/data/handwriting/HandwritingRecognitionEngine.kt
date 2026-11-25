package com.jukhg10.hanzimemo.data.handwriting

import android.graphics.Bitmap
import com.jukhg10.hanzimemo.data.HanziRecognitionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Resultado de reconocimiento caligráfico que incluye caracteres candidatos
 * y evaluación de coincidencia con el carácter objetivo de estudio.
 */
data class HandwritingRecognitionResult(
    val candidates: List<String>,
    val bestMatch: String? = candidates.firstOrNull(),
    val isTargetMatch: Boolean = false,
    val confidenceHint: String = ""
)

/**
 * Motor de reconocimiento de escritura a mano en tiempo real 100% offline.
 * Utiliza Google ML Kit Chinese Text Recognition sobre el bitmap de trazos del lienzo.
 */
@Singleton
class HandwritingRecognitionEngine @Inject constructor(
    private val recognitionService: HanziRecognitionService
) {

    // Rango Unicode de caracteres chinos unificados CJK
    private val cjkRegex = Regex("[\u4E00-\u9FA5\u3400-\u4DB5\u20000-\u2A6DF]")

    /**
     * Procesa el mapa de bits del lienzo y retorna la lista ordenada de caracteres chinos detectados.
     */
    suspend fun recognizeFromBitmap(
        bitmap: Bitmap,
        targetCharacter: String? = null
    ): HandwritingRecognitionResult = withContext(Dispatchers.Default) {
        val rawText = recognitionService.recognizeTextFromBitmap(bitmap) ?: ""
        
        // Extraer caracteres chinos únicos descartando signos de puntuación occidentales o ruido
        val detectedChars = mutableListOf<String>()
        for (ch in rawText) {
            val s = ch.toString()
            if (cjkRegex.matches(s) && !detectedChars.contains(s)) {
                detectedChars.add(s)
            }
        }

        val target = targetCharacter?.trim() ?: ""
        val isMatch = if (target.isNotEmpty()) {
            detectedChars.any { it == target }
        } else {
            false
        }

        val hint = when {
            target.isEmpty() -> if (detectedChars.isNotEmpty()) "Detectado: ${detectedChars.joinToString(", ")}" else "Traza un carácter"
            isMatch -> "¡Coincidencia exacta con '$target'!"
            detectedChars.isNotEmpty() -> "Detectado '${detectedChars.first()}', el objetivo es '$target'"
            else -> "Continúa trazando para evaluar"
        }

        HandwritingRecognitionResult(
            candidates = detectedChars,
            bestMatch = detectedChars.firstOrNull(),
            isTargetMatch = isMatch,
            confidenceHint = hint
        )
    }

    /**
     * Evalúa si un carácter dibujado coincide con el objetivo.
     */
    fun matchesTarget(target: String, candidate: String): Boolean {
        return target.trim() == candidate.trim()
    }
}

