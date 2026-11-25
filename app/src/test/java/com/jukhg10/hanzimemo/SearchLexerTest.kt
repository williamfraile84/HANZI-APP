package com.jukhg10.hanzimemo

import com.jukhg10.hanzimemo.domain.search.SearchLexer
import com.jukhg10.hanzimemo.domain.search.TokenType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchLexerTest {

    @Test
    fun `lex entrada vacia retorna lista vacia`() {
        val result = SearchLexer.lex("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `lex entrada solo espacios retorna lista vacia`() {
        val result = SearchLexer.lex("   ")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `lex texto simple sin prefijos retorna GENERAL_SEARCH`() {
        val input = "ni hao"
        val tokens = SearchLexer.lex(input)

        assertEquals(1, tokens.size)
        assertEquals(TokenType.GENERAL_SEARCH, tokens[0].type)
        assertEquals("ni hao", tokens[0].value)
    }

    @Test
    fun `lex prefijo pinyin p con dos letras reconoce PINYIN_SEARCH`() {
        val input = "p:ni1 hao3"
        val tokens = SearchLexer.lex(input)

        assertEquals(1, tokens.size)
        assertEquals(TokenType.PINYIN_SEARCH, tokens[0].type)
        assertEquals("ni1 hao3", tokens[0].value)
    }

    @Test
    fun `lex prefijo definition d reconoce DEFINITION_SEARCH`() {
        val input = "d:hello world"
        val tokens = SearchLexer.lex(input)

        assertEquals(1, tokens.size)
        assertEquals(TokenType.DEFINITION_SEARCH, tokens[0].type)
        assertEquals("hello world", tokens[0].value)
    }

    @Test
    fun `lex prefijo hanzi h reconoce HANZI_SEARCH`() {
        val input = "h:你好"
        val tokens = SearchLexer.lex(input)

        assertEquals(1, tokens.size)
        assertEquals(TokenType.HANZI_SEARCH, tokens[0].type)
        assertEquals("你好", tokens[0].value)
    }

    @Test
    fun `lex multiples tokens combinados parsea correctamente`() {
        val input = "p:hao3 d:good h:好"
        val tokens = SearchLexer.lex(input)

        assertEquals(3, tokens.size)

        assertEquals(TokenType.PINYIN_SEARCH, tokens[0].type)
        assertEquals("hao3", tokens[0].value)

        assertEquals(TokenType.DEFINITION_SEARCH, tokens[1].type)
        assertEquals("good", tokens[1].value)

        assertEquals(TokenType.HANZI_SEARCH, tokens[2].type)
        assertEquals("好", tokens[2].value)
    }

    @Test
    fun `lex texto con prefijos falsos o dos puntos dentro de palabra maneja de forma segura`() {
        val input = "apple:pie"
        val tokens = SearchLexer.lex(input)

        assertEquals(1, tokens.size)
        assertEquals(TokenType.GENERAL_SEARCH, tokens[0].type)
        assertEquals("apple:pie", tokens[0].value)
    }
}