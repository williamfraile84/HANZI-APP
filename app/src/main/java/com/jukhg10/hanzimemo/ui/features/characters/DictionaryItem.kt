package com.jukhg10.hanzimemo.ui.features.characters

import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.Word

sealed interface DictionaryItem {
    data class CharacterItem(val character: Character) : DictionaryItem
    data class WordItem(val word: Word) : DictionaryItem
}