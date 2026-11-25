package com.jukhg10.hanzimemo.domain.search

enum class TokenType { 
    PINYIN_SEARCH, 
    DEFINITION_SEARCH, 
    HANZI_SEARCH, 
    GENERAL_SEARCH 
}

data class SearchToken(val type: TokenType, val value: String)