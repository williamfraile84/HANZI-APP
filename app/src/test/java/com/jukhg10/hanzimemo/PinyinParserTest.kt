package com.jukhg10.hanzimemo

import com.jukhg10.hanzimemo.data.PinyinParser
import org.junit.Assert.*
import org.junit.Test

class PinyinParserTest {

    @Test
    fun `parse silaba valida con inicial simple y tono`() {
        val result = PinyinParser.parse("ma1")
        assertNotNull(result)
        assertEquals("m", result?.initial)
        assertEquals("a", result?.`final`)
        assertEquals(1, result?.tone)
    }

    @Test
    fun `parse silaba valida con inicial compuesta zh ch sh`() {
        val resultZh = PinyinParser.parse("zhong1")
        assertNotNull(resultZh)
        assertEquals("zh", resultZh?.initial)
        assertEquals("ong", resultZh?.`final`)
        assertEquals(1, resultZh?.tone)

        val resultSh = PinyinParser.parse("shi4")
        assertNotNull(resultSh)
        assertEquals("sh", resultSh?.initial)
        assertEquals("i", resultSh?.`final`)
        assertEquals(4, resultSh?.tone)
    }

    @Test
    fun `parse silaba sin inicial (vocal pura)`() {
        val result = PinyinParser.parse("ai3")
        assertNotNull(result)
        assertNull(result?.initial)
        assertEquals("ai", result?.`final`)
        assertEquals(3, result?.tone)
    }

    @Test
    fun `parse silaba invalida sin digito de tono retorna null`() {
        val result = PinyinParser.parse("hao")
        assertNull(result)
    }

    @Test
    fun `parse silaba invalida con final no existente retorna null`() {
        val result = PinyinParser.parse("bxyz1")
        assertNull(result)
    }

    @Test
    fun `validateToken acepta multiples silabas separadas por espacio`() {
        val isValid = PinyinParser.validateToken("ni3 hao3")
        assertTrue(isValid)
    }

    @Test
    fun `validateToken rechaza cadena con al menos una silaba invalida`() {
        val isValid = PinyinParser.validateToken("ni3 hao4 xyz")
        assertFalse(isValid)
    }
}