package com.jukhg10.hanzimemo.data.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor de síntesis de voz (TTS) especializado en la pronunciación
 * de caracteres chinos y Pinyin utilizando el motor nativo de Android.
 */
@Singleton
class TextToSpeechManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.CHINA)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeechManager", "El idioma chino no está disponible o falta el paquete de voz.")
            } else {
                isInitialized = true
                tts?.setSpeechRate(0.85f) // Velocidad ligeramente pausada para aprendizaje
            }
        } else {
            Log.e("TextToSpeechManager", "Error al inicializar el servicio TextToSpeech.")
        }
    }

    /**
     * Pronuncia el texto en chino mandarín (Hanzi o Pinyin).
     */
    fun speak(text: String) {
        if (isInitialized && text.isNotBlank()) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "HanziMemoSpeechId")
        } else {
            Log.w("TextToSpeechManager", "TTS no inicializado aún, reintentando...")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}