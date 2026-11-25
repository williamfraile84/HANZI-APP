package com.jukhg10.hanzimemo.domain.models

/**
 * Representa los componentes individuales que integran un carácter Hanzi
 * (ej. 氵 Agua + 木 Árbol = 沐 Bañarse)
 */
data class CharacterComponentInfo(
    val component: String,
    val name: String,
    val pinyin: String,
    val meaning: String,
    val role: ComponentRole,
    val colorHex: String
)

enum class ComponentRole {
    RADICAL_SEMANTIC,
    PHONETIC,
    HISTORICAL_PICTOGRAPH,
    OTHER
}

/**
 * Modelo extendido de carácter que contiene todos los recursos pedagógicos
 * necesarios para la experiencia visual de flashcard superior.
 */
data class EnhancedCharacterInfo(
    val id: Int,
    val simplified: String,
    val traditional: String,
    val pinyin: String,
    val pinyinTone: Int,
    val primaryDefinition: String,
    val secondaryDefinitions: List<String>,
    val hskLevel: Int,
    val radical: String,
    val strokeCount: Int,
    val strokeOrderPath: String,
    val illustrationUrlOrRes: String?,
    val components: List<CharacterComponentInfo>,
    val etymologyOrigin: String,
    val historicalEvolutionNotes: String,
    val mnemonicStory: String,
    val whyIllustrationHelps: String,
    val frequentWords: List<String>,
    val exampleSentences: List<ExampleSentence>,
    val culturalNotes: String?,
    val curatedSpanishMeanings: List<String> = emptyList()
)

data class ExampleSentence(
    val chinese: String,
    val pinyin: String,
    val spanish: String
)