package com.jukhg10.hanzimemo.domain.usecase

import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.Word
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import com.jukhg10.hanzimemo.domain.search.SearchToken
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchDictionaryUseCase @Inject constructor(
    private val repository: IDictionaryRepository
) {
    fun searchCharacters(tokens: List<SearchToken>): Flow<List<Character>> {
        return repository.searchCharactersByTokens(tokens)
    }

    fun searchWords(tokens: List<SearchToken>): Flow<List<Word>> {
        return repository.searchWordsByTokens(tokens)
    }
}