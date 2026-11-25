package com.jukhg10.hanzimemo.data

/**
 * Utilidad de alto rendimiento para el formateo y embellecimiento de Pinyin.
 * Convierte notación numérica CC-CEDICT (ej. "ni3 hao3", "A1 / a1 / e1", "hao3 / hao4")
 * a Pinyin estandarizado con marcas diacríticas tonales oficiales (ej. "nǐ hǎo", "Ā / ā / ē", "hǎo / hào").
 */
object PinyinFormatter {

    private val toneMap = mapOf(
        'a' to listOf('ā', 'á', 'ǎ', 'à', 'a'),
        'e' to listOf('ē', 'é', 'ě', 'è', 'e'),
        'i' to listOf('ī', 'í', 'ǐ', 'ì', 'i'),
        'o' to listOf('ō', 'ó', 'ǒ', 'ò', 'o'),
        'u' to listOf('ū', 'ú', 'ǔ', 'ù', 'u'),
        'ü' to listOf('ǖ', 'ǘ', 'ǚ', 'ǜ', 'ü'),
        'v' to listOf('ǖ', 'ǘ', 'ǚ', 'ǜ', 'ü'),
        'A' to listOf('Ā', 'Á', 'Ǎ', 'À', 'A'),
        'E' to listOf('Ē', 'É', 'Ě', 'È', 'E'),
        'I' to listOf('Ī', 'Í', 'Ǐ', 'Ì', 'I'),
        'O' to listOf('Ō', 'Ó', 'Ǒ', 'Ò', 'O'),
        'U' to listOf('Ū', 'Ú', 'Ǔ', 'Ù', 'U'),
        'Ü' to listOf('Ǖ', 'Ǘ', 'Ǚ', 'Ǜ', 'Ü'),
        'V' to listOf('Ǖ', 'Ǘ', 'Ǚ', 'Ǜ', 'Ü')
    )

    private val syllableRegex = Regex("([a-zA-ZüÜ:]+?)([1-5])")

    /**
     * Aplica la marca diacrítica al tono correspondiente en una sola sílaba.
     */
    fun formatSyllable(syllable: String, tone: Int): String {
        if (tone !in 1..5) return syllable
        val toneIndex = tone - 1

        var norm = syllable
            .replace("u:", "ü")
            .replace("U:", "Ü")
            .replace("v", "ü")
            .replace("V", "Ü")

        // Regla 1: Si contiene 'a' o 'e', lleva el acento
        for (vowel in listOf('a', 'e', 'A', 'E')) {
            val idx = norm.indexOf(vowel)
            if (idx != -1) {
                val replacement = toneMap[vowel]?.get(toneIndex) ?: vowel
                return norm.substring(0, idx) + replacement + norm.substring(idx + 1)
            }
        }

        // Regla 2: Si contiene 'ou', la 'o' lleva el acento
        val ouLower = norm.indexOf("ou")
        if (ouLower != -1) {
            val replacement = toneMap['o']?.get(toneIndex) ?: 'o'
            return norm.substring(0, ouLower) + replacement + norm.substring(ouLower + 1)
        }
        val ouUpper = norm.indexOf("Ou")
        if (ouUpper != -1) {
            val replacement = toneMap['O']?.get(toneIndex) ?: 'O'
            return norm.substring(0, ouUpper) + replacement + norm.substring(ouUpper + 1)
        }

        // Regla 3: En los demás casos, la última vocal lleva el tono (ui -> uì, iu -> iù)
        var lastVowelIdx = -1
        var targetVowel = ' '
        for (i in norm.indices) {
            val ch = norm[i]
            if (ch in toneMap) {
                lastVowelIdx = i
                targetVowel = ch
            }
        }

        if (lastVowelIdx != -1) {
            val replacement = toneMap[targetVowel]?.get(toneIndex) ?: targetVowel
            return norm.substring(0, lastVowelIdx) + replacement + norm.substring(lastVowelIdx + 1)
        }

        return norm
    }

    /**
     * Formatea cualquier cadena de Pinyin (con una o varias sílabas, barras o espacios).
     * Ejemplo: "A1 / a1 / e1" -> "Ā / ā / ē"
     *           "ni3 hao3" -> "nǐ hǎo"
     *           "A1 bian3" -> "Ā biǎn"
     */
    fun format(rawPinyin: String?): String {
        if (rawPinyin.isNullOrBlank()) return ""

        return syllableRegex.replace(rawPinyin) { matchResult ->
            val syl = matchResult.groupValues[1]
            val tone = matchResult.groupValues[2].toIntOrNull() ?: 5
            formatSyllable(syl, tone)
        }
    }
}

