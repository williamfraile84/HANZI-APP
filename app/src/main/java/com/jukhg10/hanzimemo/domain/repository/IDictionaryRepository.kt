package com.jukhg10.hanzimemo.domain.repository

import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.CharacterDetail
import com.jukhg10.hanzimemo.data.models.Word
import com.jukhg10.hanzimemo.data.models.StudyItem
import com.jukhg10.hanzimemo.data.repository.ReviewPair
import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo
import com.jukhg10.hanzimemo.domain.search.SearchToken
import com.jukhg10.hanzimemo.ui.features.characters.DictionaryItem
import kotlinx.coroutines.flow.Flow

interface IDictionaryRepository {
    fun getAllCharacters(): Flow<List<Character>>
    fun getAllWords(): Flow<List<Word>>
    
    suspend fun getCharacterById(id: Int): Character?
    suspend fun getWordById(id: Int): Word?

    suspend fun addItemToStudyList(item: StudyItem): Long
    suspend fun removeItemFromStudyList(itemId: Int, itemType: String)
    fun isItemBeingStudied(itemId: Int, itemType: String): Flow<Boolean>
    suspend fun getStudyItemByDictionaryId(itemId: Int, itemType: String): StudyItem?
    suspend fun findOrCreateStudyItem(itemId: Int, itemType: String): StudyItem?
    
    fun getFullStudyListItems(): Flow<List<DictionaryItem>>
    fun getFullStudyListWithStatus(): Flow<List<ReviewPair>>
    suspend fun getReviewDeck(): List<ReviewPair>
    suspend fun getStudyItemById(studyItemId: Int): ReviewPair?
    
    suspend fun updateStudyItemProgress(studyItem: StudyItem, knewIt: Boolean)
    suspend fun markItemAsLearned(studyItemId: Int, learned: Boolean)
    suspend fun decrementItemPriority(studyItem: StudyItem)
    suspend fun onKnewIt(studyItem: StudyItem)
    suspend fun onForgot(studyItem: StudyItem)
    
    fun searchCharactersByTokens(tokens: List<SearchToken>): Flow<List<Character>>
    fun searchWordsByTokens(tokens: List<SearchToken>): Flow<List<Word>>
    fun clearCaches()

    // --- Nuevos Métodos para el Sistema de Flashcards Visuales y Mnemotécnicas ---
    suspend fun getEnhancedCharacterInfo(characterId: Int): EnhancedCharacterInfo?
    suspend fun saveOrUpdateCharacterDetail(detail: CharacterDetail): Long
}
