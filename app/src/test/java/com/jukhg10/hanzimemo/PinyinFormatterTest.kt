package com.jukhg10.hanzimemo

import com.jukhg10.hanzimemo.data.PinyinFormatter
import org.junit.Assert.assertEquals
import org.junit.Test

class PinyinFormatterTest {

    @Test
    fun testSingleVowelTones() {
        assertEquals("ā", PinyinFormatter.format("a1"))
        assertEquals("á", PinyinFormatter.format("a2"))
        assertEquals("ǎ", PinyinFormatter.format("a3"))
        assertEquals("à", PinyinFormatter.format("a4"))
        assertEquals("a", PinyinFormatter.format("a5"))
    }

    @Test
    fun testCapitalizedTones() {
        assertEquals("Ā", PinyinFormatter.format("A1"))
        assertEquals("Hǎo", PinyinFormatter.format("Hao3"))
        assertEquals("Zhōng", PinyinFormatter.format("Zhong1"))
    }

    @Test
    fun testMultipleSyllablesWithSlash() {
        assertEquals("Ā / ā / ē", PinyinFormatter.format("A1 / a1 / e1"))
        assertEquals("hǎo / hào", PinyinFormatter.format("hao3 / hao4"))
        assertEquals("kān / kàn", PinyinFormatter.format("kan1 / kan4"))
    }

    @Test
    fun testPhrasesAndCompoundWords() {
        assertEquals("nǐ hǎo", PinyinFormatter.format("ni3 hao3"))
        assertEquals("Ā biǎn", PinyinFormatter.format("A1 bian3"))
        assertEquals("rén", PinyinFormatter.format("ren2"))
        assertEquals("shuǐ", PinyinFormatter.format("shui3"))
        assertEquals("liù", PinyinFormatter.format("liu4"))
    }

    @Test
    fun testUmlautVowels() {
        assertEquals("lǚ", PinyinFormatter.format("lv3"))
        assertEquals("nǚ", PinyinFormatter.format("nu:3"))
    }
}

