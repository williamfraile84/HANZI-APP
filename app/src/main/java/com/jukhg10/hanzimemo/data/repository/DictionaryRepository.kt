package com.jukhg10.hanzimemo.data.repository

import android.util.Log

import android.util.LruCache

import androidx.sqlite.db.SimpleSQLiteQuery

import com.jukhg10.hanzimemo.data.database.CharacterDao

import com.jukhg10.hanzimemo.data.database.CharacterDetailDao

import com.jukhg10.hanzimemo.data.database.WordDao

import com.jukhg10.hanzimemo.data.database.StudyDao

import com.jukhg10.hanzimemo.data.illustrations.IllustrationEngine

import com.jukhg10.hanzimemo.data.models.Character

import com.jukhg10.hanzimemo.data.models.CharacterDetail

import com.jukhg10.hanzimemo.data.models.Word

import com.jukhg10.hanzimemo.data.models.StudyItem

import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo

import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository

import com.jukhg10.hanzimemo.domain.search.SearchToken

import com.jukhg10.hanzimemo.domain.search.TokenType

import com.jukhg10.hanzimemo.ui.features.characters.DictionaryItem

import kotlinx.coroutines.CoroutineDispatcher

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.flowOf

import kotlinx.coroutines.flow.flowOn

import kotlinx.coroutines.flow.map

import kotlinx.coroutines.withContext

import javax.inject.Inject

import javax.inject.Singleton

data class ReviewPair(val studyItem: StudyItem, val dictionaryItem: DictionaryItem)

@Singleton

class DictionaryRepository @Inject constructor(

private val characterDao: CharacterDao,

private val wordDao: WordDao,

private val studyDao: StudyDao,

private val characterDetailDao: CharacterDetailDao,

private val illustrationEngine: IllustrationEngine,

private val dispatcher: CoroutineDispatcher = Dispatchers.IO

) : IDictionaryRepository {

private val characterSearchCache = LruCache<String, List<Character>>(16)

private val wordSearchCache = LruCache<String, List<Word>>(16)

override fun getAllCharacters(): Flow<List<Character>> = 

characterDao.getAllCharacters().flowOn(dispatcher)

override fun getAllWords(): Flow<List<Word>> = 

wordDao.getAllWords().flowOn(dispatcher)

override suspend fun getCharacterById(id: Int): Character? = withContext(dispatcher) {
        characterDao.getCharacterById(id)
    }

override suspend fun getWordById(id: Int): Word? = withContext(dispatcher) {
        wordDao.getWordById(id)
    }

override suspend fun addItemToStudyList(item: StudyItem): Long = withContext(dispatcher) {

studyDao.addItem(item)

}

override suspend fun removeItemFromStudyList(itemId: Int, itemType: String) = withContext(dispatcher) {

studyDao.removeItem(itemId, itemType)

}

override fun isItemBeingStudied(itemId: Int, itemType: String): Flow<Boolean> {

return studyDao.isBeingStudied(itemId, itemType).flowOn(dispatcher)

}

override suspend fun getStudyItemByDictionaryId(itemId: Int, itemType: String): StudyItem? = withContext(dispatcher) {

return@withContext studyDao.getItemByItemId(itemId, itemType)

}

override suspend fun findOrCreateStudyItem(itemId: Int, itemType: String): StudyItem? = withContext(dispatcher) {

val existing = studyDao.getItemByItemId(itemId, itemType)

if (existing != null) return@withContext existing

val newId = studyDao.addItem(StudyItem(itemId = itemId, itemType = itemType))

if (newId <= 0L) {

return@withContext studyDao.getItemByItemId(itemId, itemType)

}

return@withContext studyDao.getItemById(newId.toInt())

}

override fun getFullStudyListItems(): Flow<List<DictionaryItem>> {

return studyDao.getFullStudyListWithDetails().map { detailsList ->

val fullItems = mutableListOf<DictionaryItem>()

for (detail in detailsList) {

val dictItem: DictionaryItem? = if (detail.studyItem.itemType == "character") {

detail.character?.let { DictionaryItem.CharacterItem(it) }

} else {

detail.word?.let { DictionaryItem.WordItem(it) }

}

dictItem?.let { fullItems.add(it) }

}

fullItems

}.flowOn(dispatcher)

}

override fun getFullStudyListWithStatus(): Flow<List<ReviewPair>> {

return studyDao.getFullStudyListWithDetails().map { detailsList ->

val fullItems = mutableListOf<ReviewPair>()

for (detail in detailsList) {

val dictItem: DictionaryItem? = if (detail.studyItem.itemType == "character") {

detail.character?.let { DictionaryItem.CharacterItem(it) }

} else {

detail.word?.let { DictionaryItem.WordItem(it) }

}

dictItem?.let { 

fullItems.add(ReviewPair(studyItem = detail.studyItem, dictionaryItem = it)) 

}

}

fullItems

}.flowOn(dispatcher)

}

override suspend fun getReviewDeck(): List<ReviewPair> = withContext(dispatcher) {

val studyItems = studyDao.getStudyDeck()

val fullItems = mutableListOf<ReviewPair>()

for (item in studyItems) {

val dictItem: DictionaryItem? = if (item.itemType == "character") {

characterDao.getCharacterById(item.itemId)?.let { DictionaryItem.CharacterItem(it) }

} else {

wordDao.getWordById(item.itemId)?.let { DictionaryItem.WordItem(it) }

}

dictItem?.let { fullItems.add(ReviewPair(studyItem = item, dictionaryItem = it)) }

}

return@withContext fullItems

}

override suspend fun getStudyItemById(studyItemId: Int): ReviewPair? = withContext(dispatcher) {

val studyItem = studyDao.getItemById(studyItemId)

if (studyItem != null) {

val dictItem: DictionaryItem? = if (studyItem.itemType == "character") {

characterDao.getCharacterById(studyItem.itemId)?.let { DictionaryItem.CharacterItem(it) }

} else {

wordDao.getWordById(studyItem.itemId)?.let { DictionaryItem.WordItem(it) }

}

dictItem?.let { return@withContext ReviewPair(studyItem = studyItem, dictionaryItem = it) }

}

return@withContext null

}

override suspend fun updateStudyItemProgress(studyItem: StudyItem, knewIt: Boolean) = withContext(dispatcher) {

if (knewIt) {

val newPriority = studyItem.maxPriority + 1

studyDao.updateStudyProgress(studyItem.id, newPriority, newPriority)

} else {

val newCurrentPriority = 1

val newMaxPriority = (studyItem.maxPriority - 1).coerceAtLeast(1)

studyDao.updateStudyProgress(studyItem.id, newCurrentPriority, newMaxPriority)

}

}

override suspend fun markItemAsLearned(studyItemId: Int, learned: Boolean) = withContext(dispatcher) {

studyDao.updateLearned(studyItemId, learned)

}

override suspend fun decrementItemPriority(studyItem: StudyItem) = withContext(dispatcher) {

if (studyItem.currentPriority > 1) {

studyDao.updateCurrentPriority(studyItem.id, studyItem.currentPriority - 1)

}

}

override suspend fun onKnewIt(studyItem: StudyItem) = withContext(dispatcher) {

try {

Log.d("HanziMemo.Repo", "onKnewIt: itemId=${studyItem.id}")

val newMaxPriority = studyItem.maxPriority + 1

val newCurrentPriority = newMaxPriority

studyDao.updateStudyProgress(studyItem.id, newCurrentPriority, newMaxPriority)

studyDao.updateLearned(studyItem.id, true)

} catch (e: Exception) {

Log.e("HanziMemo.Repo", "onKnewIt failed", e)

throw e

}

}

override suspend fun onForgot(studyItem: StudyItem) = withContext(dispatcher) {

try {

Log.d("HanziMemo.Repo", "onForgot: itemId=${studyItem.id}")

val newCurrentPriority = 1

val newMaxPriority = (studyItem.maxPriority - 1).coerceAtLeast(1)

studyDao.updateStudyProgress(studyItem.id, newCurrentPriority, newMaxPriority)

studyDao.updateLearned(studyItem.id, false)

} catch (e: Exception) {

Log.e("HanziMemo.Repo", "onForgot failed", e)

throw e

}

}

override fun searchCharactersByTokens(tokens: List<SearchToken>): Flow<List<Character>> {

if (tokens.isEmpty()) return characterDao.getAllCharacters().flowOn(dispatcher)

val cacheKey = tokens.joinToString("|") { "${it.type}:${it.value}" }

val cached = characterSearchCache.get(cacheKey)

if (cached != null) return flowOf(cached)

val sqliteQuery = buildTokenSqlQuery(tableName = "characters", tokens = tokens)

return characterDao.searchCharactersByTokens(sqliteQuery).map { list ->

characterSearchCache.put(cacheKey, list)

list

}.flowOn(dispatcher)

}

override fun searchWordsByTokens(tokens: List<SearchToken>): Flow<List<Word>> {

if (tokens.isEmpty()) return wordDao.getAllWords().flowOn(dispatcher)

val cacheKey = tokens.joinToString("|") { "${it.type}:${it.value}" }

val cached = wordSearchCache.get(cacheKey)

if (cached != null) return flowOf(cached)

val sqliteQuery = buildTokenSqlQuery(tableName = "words", tokens = tokens)

return wordDao.searchWordsByTokens(sqliteQuery).map { list ->

wordSearchCache.put(cacheKey, list)

list

}.flowOn(dispatcher)

}

private fun buildTokenSqlQuery(tableName: String, tokens: List<SearchToken>): SimpleSQLiteQuery {

val queryBuilder = StringBuilder("SELECT * FROM $tableName WHERE ")

val args = mutableListOf<Any>()

tokens.forEachIndexed { index, token ->

if (index > 0) queryBuilder.append(" AND ")

val searchValue = token.value.trim().lowercase()

when (token.type) {

TokenType.PINYIN_SEARCH -> {

val queryWithoutTone = searchValue.replace(Regex("[12345]"), "")

val normalizedPinyin = "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(pinyin), '1', ''), '2', ''), '3', ''), '4', ''), '5', '')"

queryBuilder.append("(LOWER(pinyin) LIKE ? OR $normalizedPinyin LIKE ?)")

args.add("%$searchValue%")

args.add("%$queryWithoutTone%")

}

TokenType.DEFINITION_SEARCH -> {

queryBuilder.append("LOWER(definition) LIKE ?")

args.add("%$searchValue%")

}

TokenType.HANZI_SEARCH -> {

queryBuilder.append("(LOWER(simplified) LIKE ? OR LOWER(traditional) LIKE ?)")

args.add("%$searchValue%")

args.add("%$searchValue%")

}

TokenType.GENERAL_SEARCH -> {

val normalizedPinyin = "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(pinyin), '1', ''), '2', ''), '3', ''), '4', ''), '5', '')"

queryBuilder.append("(LOWER(simplified) LIKE ? OR LOWER(traditional) LIKE ? OR LOWER(definition) LIKE ? OR LOWER(pinyin) LIKE ? OR $normalizedPinyin LIKE ?)")

args.add("%$searchValue%")

args.add("%$searchValue%")

args.add("%$searchValue%")

args.add("%$searchValue%")

args.add("%$searchValue%")

}

}

}

val firstToken = tokens.firstOrNull()?.value?.trim()?.lowercase() ?: ""

val firstWithoutTone = firstToken.replace(Regex("[12345]"), "")

val normPinyinExpr = "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(pinyin), '1', ''), '2', ''), '3', ''), '4', ''), '5', '')"

val hskOrderClause = if (tableName == "characters") {

"(SELECT CASE WHEN hsk_level > 0 THEN hsk_level ELSE 99 END FROM character_details WHERE character_id = characters.id) ASC,"

} else {

""

}

queryBuilder.append("""

ORDER BY 

CASE 

WHEN LOWER(simplified) = ? THEN 0

WHEN LOWER(simplified) LIKE ? THEN 1

WHEN LOWER(pinyin) = ? 

OR $normPinyinExpr = ? 

OR $normPinyinExpr LIKE ? || ' /%' 

OR $normPinyinExpr LIKE '%/ ' || ? || ' /%' 

OR $normPinyinExpr LIKE '%/ ' || ? THEN 2

WHEN LOWER(pinyin) LIKE ? OR $normPinyinExpr LIKE ? THEN 3

WHEN LOWER(definition) LIKE ? THEN 4

ELSE 5

END,

$hskOrderClause

LENGTH(simplified) ASC,

LOWER(pinyin) ASC 

LIMIT 200

""".trimIndent())

args.add(firstToken)

args.add("$firstToken%")

args.add(firstToken)

args.add(firstWithoutTone)

args.add(firstWithoutTone)

args.add(firstWithoutTone)

args.add(firstWithoutTone)

args.add("$firstToken%")

args.add("$firstWithoutTone%")

args.add("%$firstToken%")

return SimpleSQLiteQuery(queryBuilder.toString(), args.toTypedArray())

}

override fun clearCaches() {

characterSearchCache.evictAll()

wordSearchCache.evictAll()

Log.d("SearchCache", "🧹 Caché limpiado completamente")

}

// --- Implementación de nuevos métodos enriquecidos ---

override suspend fun getEnhancedCharacterInfo(characterId: Int): EnhancedCharacterInfo? = withContext(dispatcher) {

val character = characterDao.getCharacterById(characterId) ?: return@withContext null

val detail = characterDetailDao.getDetailByCharacterId(characterId)

return@withContext illustrationEngine.buildEnhancedCharacterInfo(character, detail)

}

override suspend fun saveOrUpdateCharacterDetail(detail: CharacterDetail): Long = withContext(dispatcher) {

characterDetailDao.insertOrUpdateDetail(detail)

}

}