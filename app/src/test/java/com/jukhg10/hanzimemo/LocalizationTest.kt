package com.jukhg10.hanzimemo

import com.jukhg10.hanzimemo.data.preferences.AppLanguage
import com.jukhg10.hanzimemo.domain.models.ComponentRole
import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo
import com.jukhg10.hanzimemo.ui.i18n.RadicalL10nData
import com.jukhg10.hanzimemo.ui.i18n.StringsEn
import com.jukhg10.hanzimemo.ui.i18n.StringsEs
import com.jukhg10.hanzimemo.ui.i18n.StringsZh
import com.jukhg10.hanzimemo.ui.i18n.getStringsForLanguage
import org.junit.Assert.*
import org.junit.Test

class LocalizationTest {

    @Test
    fun `getStringsForLanguage retorna la instancia correcta para cada idioma`() {
        assertSame(StringsEs, getStringsForLanguage(AppLanguage.SPANISH))
        assertSame(StringsEn, getStringsForLanguage(AppLanguage.ENGLISH))
        assertSame(StringsZh, getStringsForLanguage(AppLanguage.CHINESE))
    }

    @Test
    fun `las cadenas de navegacion no estan vacias en ningun idioma`() {
        val allStrings = listOf(StringsEs, StringsEn, StringsZh)
        for (s in allStrings) {
            assertTrue(s.navDictionary.isNotBlank())
            assertTrue(s.navFlashcards.isNotBlank())
            assertTrue(s.navCalligraphy.isNotBlank())
            assertTrue(s.navCamera.isNotBlank())
            assertTrue(s.navProgress.isNotBlank())
            assertTrue(s.navAbout.isNotBlank())
        }
    }

    @Test
    fun `formato de radicales conocidos retorna significados traducidos`() {
        // En español
        val meaningEs = RadicalL10nData.getRadicalMeaning("亻", "persona", AppLanguage.SPANISH)
        assertEquals("persona (radical lateral)", meaningEs)

        // En inglés
        val meaningEn = RadicalL10nData.getRadicalMeaning("亻", "person", AppLanguage.ENGLISH)
        assertEquals("person radical", meaningEn)

        // En chino
        val meaningZh = RadicalL10nData.getRadicalMeaning("亻", "人", AppLanguage.CHINESE)
        assertEquals("单人旁", meaningZh)
    }

    @Test
    fun `formato de radical desconocido retorna el valor por defecto`() {
        val fallback = RadicalL10nData.getRadicalMeaning("xyz", "default meaning", AppLanguage.SPANISH)
        assertEquals("default meaning", fallback)
    }

    @Test
    fun `formato de definicion reemplaza terminologia conocida segun idioma`() {
        val raw = "good / well / proper"
        val formattedEs = RadicalL10nData.formatL10nDefinition(raw, AppLanguage.SPANISH)
        assertTrue(formattedEs.isNotBlank())

        val formattedEn = RadicalL10nData.formatL10nDefinition(raw, AppLanguage.ENGLISH)
        assertEquals("good; well; proper", formattedEn)

        val formattedZh = RadicalL10nData.formatL10nDefinition(raw, AppLanguage.CHINESE)
        assertTrue(formattedZh.isNotBlank())
    }

    @Test
    fun `formato de historia mnemotecnica genera texto coherente`() {
        val sampleInfo = EnhancedCharacterInfo(
            id = 1,
            simplified = "休",
            traditional = "休",
            pinyin = "xiū",
            pinyinTone = 1,
            primaryDefinition = "descansar",
            secondaryDefinitions = emptyList(),
            hskLevel = 1,
            radical = "亻",
            strokeCount = 6,
            strokeOrderPath = "",
            illustrationUrlOrRes = null,
            components = emptyList(),
            etymologyOrigin = "Persona junto a un árbol",
            historicalEvolutionNotes = "",
            mnemonicStory = "Una persona descansando junto a un árbol.",
            whyIllustrationHelps = "",
            frequentWords = emptyList(),
            exampleSentences = emptyList(),
            culturalNotes = null,
            curatedSpanishMeanings = listOf("descansar")
        )

        val storyEs = RadicalL10nData.formatMnemonicStory(sampleInfo, AppLanguage.SPANISH)
        assertTrue(storyEs.contains("persona") || storyEs.contains("árbol") || storyEs.contains("descans"))

        val storyEn = RadicalL10nData.formatMnemonicStory(sampleInfo, AppLanguage.ENGLISH)
        assertTrue(storyEn.isNotBlank())

        val storyZh = RadicalL10nData.formatMnemonicStory(sampleInfo, AppLanguage.CHINESE)
        assertTrue(storyZh.isNotBlank())
    }

    @Test
    fun `formato de oracion de ejemplo conserva la oracion en espanol y traduce en ingles y chino`() {
        val chinese = "你好"
        val pinyin = "nǐ hǎo"
        val spanish = "Esta oración ejemplifica la estructura gramatical habitual de 你好"

        val resEs = RadicalL10nData.formatL10nExampleSentence(chinese, pinyin, spanish, AppLanguage.SPANISH)
        assertEquals(spanish, resEs)

        val resEn = RadicalL10nData.formatL10nExampleSentence(chinese, pinyin, spanish, AppLanguage.ENGLISH)
        assertTrue(resEn.contains("grammatical structure") || resEn.contains("Hello") || resEn.contains("example"))

        val resZh = RadicalL10nData.formatL10nExampleSentence(chinese, pinyin, spanish, AppLanguage.CHINESE)
        assertTrue(resZh.isNotBlank())
    }
}
