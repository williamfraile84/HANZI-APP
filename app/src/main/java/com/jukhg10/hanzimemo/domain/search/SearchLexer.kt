package com.jukhg10.hanzimemo.domain.search

object SearchLexer {

    private enum class State {
        INITIAL,
        PREFIX_P,
        PREFIX_D,
        PREFIX_H,
        COLLECTING_TOKEN
    }

    fun lex(input: String): List<SearchToken> {
        val text = input.trim()
        if (text.isEmpty()) return emptyList()

        if (!text.contains("p:", ignoreCase = true) && 
            !text.contains("d:", ignoreCase = true) && 
            !text.contains("h:", ignoreCase = true)) {
            return listOf(SearchToken(TokenType.GENERAL_SEARCH, normalizeSpaces(text)))
        }

        val tokens = mutableListOf<SearchToken>()
        val buffer = StringBuilder()
        var currentState = State.INITIAL
        var currentType: TokenType? = null
        var pos = 0

        while (pos < text.length) {
            val char = text[pos]

            when (currentState) {
                State.INITIAL -> {
                    when {
                        char.isWhitespace() -> { pos++ }
                        char == 'p' || char == 'P' -> {
                            currentState = State.PREFIX_P
                            pos++
                        }
                        char == 'd' || char == 'D' -> {
                            currentState = State.PREFIX_D
                            pos++
                        }
                        char == 'h' || char == 'H' -> {
                            currentState = State.PREFIX_H
                            pos++
                        }
                        else -> {
                            currentType = TokenType.GENERAL_SEARCH
                            buffer.append(char)
                            currentState = State.COLLECTING_TOKEN
                            pos++
                        }
                    }
                }

                State.PREFIX_P -> {
                    if (char == ':') {
                        emitToken(tokens, currentType, buffer)
                        currentType = TokenType.PINYIN_SEARCH
                        currentState = State.COLLECTING_TOKEN
                    } else {
                        currentType = currentType ?: TokenType.GENERAL_SEARCH
                        buffer.append('p').append(char)
                        currentState = State.COLLECTING_TOKEN
                    }
                    pos++
                }

                State.PREFIX_D -> {
                    if (char == ':') {
                        emitToken(tokens, currentType, buffer)
                        currentType = TokenType.DEFINITION_SEARCH
                        currentState = State.COLLECTING_TOKEN
                    } else {
                        currentType = currentType ?: TokenType.GENERAL_SEARCH
                        buffer.append('d').append(char)
                        currentState = State.COLLECTING_TOKEN
                    }
                    pos++
                }

                State.PREFIX_H -> {
                    if (char == ':') {
                        emitToken(tokens, currentType, buffer)
                        currentType = TokenType.HANZI_SEARCH
                        currentState = State.COLLECTING_TOKEN
                    } else {
                        currentType = currentType ?: TokenType.GENERAL_SEARCH
                        buffer.append('h').append(char)
                        currentState = State.COLLECTING_TOKEN
                    }
                    pos++
                }

                State.COLLECTING_TOKEN -> {
                    val remaining = text.length - pos
                    val isNextPrefix = remaining >= 2 && text[pos + 1] == ':' && 
                            (char == 'p' || char == 'P' || char == 'd' || char == 'D' || char == 'h' || char == 'H')

                    if (isNextPrefix && char.isWhitespace().not()) {
                        emitToken(tokens, currentType, buffer)
                        currentState = State.INITIAL 
                    } else {
                        buffer.append(char)
                        pos++
                    }
                }
            }
        }

        emitToken(tokens, currentType, buffer)
        return tokens
    }

    private fun emitToken(tokens: MutableList<SearchToken>, type: TokenType?, buffer: StringBuilder) {
        if (buffer.isNotEmpty()) {
            val finalType = type ?: TokenType.GENERAL_SEARCH
            val finalValue = normalizeSpaces(buffer.toString())
            if (finalValue.isNotEmpty()) {
                tokens.add(SearchToken(finalType, finalValue))
            }
            buffer.clear()
        }
    }

    private fun normalizeSpaces(s: String) = s.replace(Regex("\\s+"), " ").trim()
}