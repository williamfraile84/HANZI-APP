package com.jukhg10.hanzimemo.data.illustrations

import android.content.Context
import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.CharacterDetail
import com.jukhg10.hanzimemo.domain.models.CharacterComponentInfo
import com.jukhg10.hanzimemo.domain.models.ComponentRole
import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo
import com.jukhg10.hanzimemo.domain.models.ExampleSentence
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Motor encargado del procesamiento mnemotécnico, resolución de ilustraciones locales,
 * descomposición estructural de radicales y generación lingüística dinámica.
 */
@Singleton
class IllustrationEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private data class KnownRadical(
        val symbol: String,
        val name: String,
        val pinyin: String,
        val meaning: String,
        val role: ComponentRole,
        val colorHex: String
    )

    private val radicalDictionary = listOf(
        KnownRadical("亻", "Persona (Radical)", "rén", "persona, ser humano", ComponentRole.RADICAL_SEMANTIC, "#E74C3C"),
        KnownRadical("人", "Persona", "rén", "persona, individuo", ComponentRole.RADICAL_SEMANTIC, "#E74C3C"),
        KnownRadical("氵", "Agua (Radical)", "shuǐ", "agua, líquido, fluido", ComponentRole.RADICAL_SEMANTIC, "#3498DB"),
        KnownRadical("水", "Agua", "shuǐ", "agua", ComponentRole.RADICAL_SEMANTIC, "#3498DB"),
        KnownRadical("木", "Árbol / Madera", "mù", "árbol, madera, vegetal", ComponentRole.RADICAL_SEMANTIC, "#2ECC71"),
        KnownRadical("火", "Fuego", "huǒ", "fuego, calor, luz", ComponentRole.RADICAL_SEMANTIC, "#E74C3C"),
        KnownRadical("灬", "Fuego (Radical inferior)", "huǒ", "fuego, cocción", ComponentRole.RADICAL_SEMANTIC, "#E74C3C"),
        KnownRadical("扌", "Mano (Radical)", "shǒu", "mano, acción manual", ComponentRole.RADICAL_SEMANTIC, "#F39C12"),
        KnownRadical("手", "Mano", "shǒu", "mano, destreza", ComponentRole.RADICAL_SEMANTIC, "#F39C12"),
        KnownRadical("讠", "Habla / Palabra (Radical)", "yán", "palabra, lenguaje, decir", ComponentRole.RADICAL_SEMANTIC, "#9B59B6"),
        KnownRadical("言", "Palabra / Discurso", "yán", "palabras, comunicación", ComponentRole.RADICAL_SEMANTIC, "#9B59B6"),
        KnownRadical("女", "Mujer", "nǚ", "mujer, femenino", ComponentRole.RADICAL_SEMANTIC, "#E84393"),
        KnownRadical("忄", "Corazón / Mente (Radical)", "xīn", "sentimiento, emoción, mente", ComponentRole.RADICAL_SEMANTIC, "#D63031"),
        KnownRadical("心", "Corazón", "xīn", "corazón, afecto", ComponentRole.RADICAL_SEMANTIC, "#D63031"),
        KnownRadical("日", "Sol / Día", "rì", "sol, día, tiempo", ComponentRole.RADICAL_SEMANTIC, "#F1C40F"),
        KnownRadical("月", "Luna / Mes", "yuè", "luna, mes, carne/cuerpo", ComponentRole.RADICAL_SEMANTIC, "#00CEC9"),
        KnownRadical("山", "Montaña", "shān", "montaña, firmeza", ComponentRole.RADICAL_SEMANTIC, "#27AE60"),
        KnownRadical("田", "Campo de cultivo", "tián", "campo, agricultura", ComponentRole.RADICAL_SEMANTIC, "#78E08F"),
        KnownRadical("口", "Boca", "kǒu", "boca, abertura, entrada", ComponentRole.RADICAL_SEMANTIC, "#E67E22"),
        KnownRadical("宀", "Techo / Casa", "mián", "techo, hogar, protección", ComponentRole.RADICAL_SEMANTIC, "#8E44AD"),
        KnownRadical("艹", "Hierba / Planta", "cǎo", "planta, vegetación", ComponentRole.RADICAL_SEMANTIC, "#2ECC71"),
        KnownRadical("⻌", "Caminar / Movimiento", "chuò", "desplazamiento, ruta", ComponentRole.RADICAL_SEMANTIC, "#E67E22"),
        KnownRadical("犭", "Animal / Perro", "quǎn", "mamífero, fauna", ComponentRole.RADICAL_SEMANTIC, "#D35400"),
        KnownRadical("刀", "Cuchillo", "dāo", "cuchillo, corte", ComponentRole.RADICAL_SEMANTIC, "#7F8C8D"),
        KnownRadical("刂", "Cuchillo (Radical)", "dāo", "corte, división", ComponentRole.RADICAL_SEMANTIC, "#7F8C8D")
    )

    fun resolveIllustrationDrawableRes(illustrationName: String?): Int? {
        if (illustrationName.isNullOrBlank()) return null
        val resId = context.resources.getIdentifier(illustrationName, "drawable", context.packageName)
        return if (resId != 0) resId else null
    }

    fun buildEnhancedCharacterInfo(
        character: Character,
        detail: CharacterDetail?
    ): EnhancedCharacterInfo {
        val pinyinClean = character.pinyin.trim()
        val tone = extractPinyinTone(pinyinClean)

        var components = parseComponentsJson(detail?.componentsJson)
        if (components.isEmpty()) {
            components = extractDynamicComponents(character)
        }

        val spanishMeanings = if (!detail?.secondaryMeanings.isNullOrBlank()) {
            detail!!.secondaryMeanings.split(";", ",").map { it.trim() }.filter { it.isNotEmpty() }
        } else {
            emptyList()
        }

        val parts = character.definition.split("/", ";").map { it.trim() }.filter { it.isNotEmpty() }
        val primaryDef = parts.firstOrNull() ?: character.definition
        val secondaryDefs = if (parts.size > 1) parts.drop(1) else emptyList()

        val frequentWords = parseFrequentWordsJson(detail?.frequentWordsJson)
        val exampleSentences = parseExampleSentencesJson(detail?.exampleSentencesJson)

        val strokeCount = if ((detail?.strokeCount ?: 0) > 0) {
            detail!!.strokeCount
        } else {
            estimateStrokeCount(character.simplified)
        }

        val radical = detail?.radical?.takeIf { it.isNotBlank() }
            ?: components.firstOrNull()?.component
            ?: character.simplified.take(1)

        val story = detail?.mnemonicStory?.takeIf { it.isNotBlank() }
            ?: generateDynamicMnemonicStory(character, components)

        val explanation = detail?.illustrationExplanation?.takeIf { it.isNotBlank() }
            ?: generateDynamicIllustrationExplanation(character, components)

        val etymology = detail?.etymologyOrigin?.takeIf { it.isNotBlank() }
            ?: generateDynamicEtymology(character, components)

        val evolution = detail?.historicalEvolution?.takeIf { it.isNotBlank() }
            ?: generateDynamicEvolution(character)

        return EnhancedCharacterInfo(
            id = character.id,
            simplified = character.simplified,
            traditional = character.traditional,
            pinyin = com.jukhg10.hanzimemo.data.PinyinFormatter.format(character.pinyin),
            pinyinTone = tone,
            primaryDefinition = primaryDef,
            secondaryDefinitions = secondaryDefs,
            hskLevel = detail?.hskLevel ?: 0,
            radical = radical,
            strokeCount = strokeCount,
            strokeOrderPath = detail?.strokeOrderData ?: "",
            illustrationUrlOrRes = detail?.illustrationResName,
            components = components,
            etymologyOrigin = etymology,
            historicalEvolutionNotes = evolution,
            mnemonicStory = story,
            whyIllustrationHelps = explanation,
            frequentWords = frequentWords,
            exampleSentences = exampleSentences,
            culturalNotes = detail?.culturalNotes,
            curatedSpanishMeanings = spanishMeanings
        )
    }

    private fun extractPinyinTone(pinyin: String): Int {
        val toneChar = pinyin.find { it.isDigit() }
        if (toneChar != null) {
            return toneChar.toString().toIntOrNull() ?: 5
        }
        if (pinyin.contains("ā") || pinyin.contains("ō") || pinyin.contains("ē") || pinyin.contains("ī") || pinyin.contains("ū") || pinyin.contains("ǖ")) return 1
        if (pinyin.contains("á") || pinyin.contains("ó") || pinyin.contains("é") || pinyin.contains("í") || pinyin.contains("ú") || pinyin.contains("ǘ")) return 2
        if (pinyin.contains("ǎ") || pinyin.contains("ǒ") || pinyin.contains("ě") || pinyin.contains("ǐ") || pinyin.contains("ǔ") || pinyin.contains("ǚ")) return 3
        if (pinyin.contains("à") || pinyin.contains("ò") || pinyin.contains("è") || pinyin.contains("ì") || pinyin.contains("ù") || pinyin.contains("ǜ")) return 4
        return 5
    }

    private fun extractDynamicComponents(character: Character): List<CharacterComponentInfo> {
        val found = mutableListOf<CharacterComponentInfo>()
        val hanzi = character.simplified

        for (rad in radicalDictionary) {
            if (hanzi.contains(rad.symbol) && rad.symbol != hanzi) {
                found.add(
                    CharacterComponentInfo(
                        component = rad.symbol,
                        name = rad.name,
                        pinyin = rad.pinyin,
                        meaning = rad.meaning,
                        role = rad.role,
                        colorHex = rad.colorHex
                    )
                )
            }
        }

        if (found.isEmpty()) {
            found.add(
                CharacterComponentInfo(
                    component = hanzi,
                    name = "Pictograma Integral",
                    pinyin = character.pinyin,
                    meaning = character.definition.split("/", ";").firstOrNull()?.trim() ?: character.definition,
                    role = ComponentRole.HISTORICAL_PICTOGRAPH,
                    colorHex = "#C0392B"
                )
            )
        }
        return found
    }

    private fun generateDynamicMnemonicStory(character: Character, components: List<CharacterComponentInfo>): String {
        if (components.size >= 2) {
            val c1 = components[0]
            val c2 = components[1]
            return "Para recordar '${character.simplified}' (${character.pinyin}): conecta el componente '${c1.component}' (${c1.meaning}) con '${c2.component}' (${c2.meaning}). Juntos forman la idea de '${character.definition}'."
        } else if (components.isNotEmpty()) {
            val c1 = components[0]
            return "Visualiza el componente radical '${c1.component}' (${c1.meaning}) actuando como la base clave para evocar el significado de '${character.definition}' en '${character.simplified}'."
        }
        return "Para recordar '${character.simplified}' (${character.pinyin}): imagina la escena gráfica del significado de '${character.definition}' asociada a la forma de sus trazos primarios."
    }

    private fun generateDynamicIllustrationExplanation(character: Character, components: List<CharacterComponentInfo>): String {
        val compNames = components.joinToString(" + ") { "'${it.component}' (${it.meaning})" }
        return "Esta tarjeta desglosa '${character.simplified}' en la combinación estructural de $compNames, permitiendo que la memoria visual asocie el dibujo con su significado principal '${character.definition}'."
    }

    private fun generateDynamicEtymology(@Suppress("UNUSED_PARAMETER") character: Character, components: List<CharacterComponentInfo>): String {
        return if (components.size >= 2) {
            "Carácter fono-semántico/ideograma compuesto que combina el radical ${components.first().component} para denotar la categoría semántica con un componente estructural para la fonética."
        } else {
            "Pictograma directo derivado del dibujo estilizado ancestral de inscripciones oraculares en huesos y bronces."
        }
    }

    private fun generateDynamicEvolution(character: Character): String {
        return "Evolución estilística: Inscripciones oraculares Jiaguwen (𓇿) ➔ Bronce Jinwen ➔ Escritura de Sello (篆书) ➔ Caligrafía Clerical (隶书) ➔ Hanzi Simplificado '${character.simplified}'."
    }

    private fun parseComponentsJson(jsonStr: String?): List<CharacterComponentInfo> {
        if (jsonStr.isNullOrBlank()) return emptyList()
        val list = mutableListOf<CharacterComponentInfo>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val comp = obj.optString("component", "")
                val name = obj.optString("name", "")
                val pinyin = obj.optString("pinyin", "")
                val meaning = obj.optString("meaning", "")
                val roleStr = obj.optString("role", "SEMANTIC")
                val colorHex = obj.optString("colorHex", "#C0392B")

                val role = when (roleStr.uppercase()) {
                    "RADICAL_SEMANTIC", "SEMANTIC" -> ComponentRole.RADICAL_SEMANTIC
                    "PHONETIC" -> ComponentRole.PHONETIC
                    "HISTORICAL_PICTOGRAPH" -> ComponentRole.HISTORICAL_PICTOGRAPH
                    else -> ComponentRole.OTHER
                }

                list.add(
                    CharacterComponentInfo(
                        component = comp,
                        name = name,
                        pinyin = pinyin,
                        meaning = meaning,
                        role = role,
                        colorHex = colorHex
                    )
                )
            }
        } catch (_: Exception) { }
        return list
    }

    private fun parseFrequentWordsJson(jsonStr: String?): List<String> {
        if (jsonStr.isNullOrBlank()) return emptyList()
        val list = mutableListOf<String>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                list.add(arr.getString(i))
            }
        } catch (_: Exception) { }
        return list
    }

    private fun parseExampleSentencesJson(jsonStr: String?): List<ExampleSentence> {
        if (jsonStr.isNullOrBlank()) return emptyList()
        val list = mutableListOf<ExampleSentence>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    ExampleSentence(
                        chinese = obj.optString("chinese", ""),
                        pinyin = obj.optString("pinyin", ""),
                        spanish = obj.optString("spanish", "")
                    )
                )
            }
        } catch (_: Exception) { }
        return list
    }

    private fun estimateStrokeCount(simplified: String): Int {
        val knownMap = mapOf(
            "一" to 1, "二" to 2, "三" to 3, "十" to 2, "人" to 2, "口" to 3, "日" to 4,
            "月" to 4, "木" to 4, "水" to 4, "火" to 4, "土" to 3, "金" to 8, "女" to 3,
            "子" to 3, "心" to 4, "手" to 4, "山" to 3, "田" to 5, "大" to 3, "小" to 3
        )
        return knownMap[simplified] ?: (simplified.length * 5).coerceAtLeast(2)
    }
}