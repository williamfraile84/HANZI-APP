package com.jukhg10.hanzimemo

import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.CharacterDetail
import com.jukhg10.hanzimemo.data.models.StudyItem
import com.jukhg10.hanzimemo.data.models.Word
import com.jukhg10.hanzimemo.data.repository.ReviewPair
import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import com.jukhg10.hanzimemo.domain.search.SearchToken
import com.jukhg10.hanzimemo.domain.search.TokenType
import com.jukhg10.hanzimemo.domain.usecase.ManageStudyListUseCase
import com.jukhg10.hanzimemo.domain.usecase.ProcessReviewAnswerUseCase
import com.jukhg10.hanzimemo.domain.usecase.SearchDictionaryUseCase
import com.jukhg10.hanzimemo.ui.features.characters.DictionaryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Mock manual liviano del Repositorio de Dominio para pruebas unitarias sin frameworks externos.
 */
class FakeDictionaryRepository : IDictionaryRepository {
    val characters = mutableListOf<Character>()
    val words = mutableListOf<Word>()
    val studyItems = mutableListOf<StudyItem>()

    override fun getAllCharacters() = flowOf(characters)
    override fun getAllWords() = flowOf(words)
    override suspend fun getCharacterById(id: Int): Character? = characters.firstOrNull { it.id == id }
    override suspend fun getWordById(id: Int): Word? = words.firstOrNull { it.id == id }

    override suspend fun addItemToStudyList(item: StudyItem): Long {
        studyItems.add(item)
        return studyItems.size.toLong()
    }

    override suspend fun removeItemFromStudyList(itemId: Int, itemType: String) {
        studyItems.removeAll { it.itemId == itemId && it.itemType == itemType }
    }

    override fun isItemBeingStudied(itemId: Int, itemType: String) =
        flowOf(studyItems.any { it.itemId == itemId && it.itemType == itemType })

    override suspend fun getStudyItemByDictionaryId(itemId: Int, itemType: String): StudyItem? {
        return studyItems.firstOrNull { it.itemId == itemId && it.itemType == itemType }
    }

    override suspend fun findOrCreateStudyItem(itemId: Int, itemType: String): StudyItem? {
        val existing = getStudyItemByDictionaryId(itemId, itemType)
        if (existing != null) return existing
        val newItem = StudyItem(id = studyItems.size + 1, itemId = itemId, itemType = itemType)
        studyItems.add(newItem)
        return newItem
    }

    override fun getFullStudyListItems() = flowOf(emptyList<DictionaryItem>())
    override fun getFullStudyListWithStatus() = flowOf(emptyList<ReviewPair>())
    override suspend fun getReviewDeck(): List<ReviewPair> = emptyList()
    override suspend fun getStudyItemById(studyItemId: Int): ReviewPair? = null

    override suspend fun updateStudyItemProgress(studyItem: StudyItem, knewIt: Boolean) {}
    override suspend fun markItemAsLearned(studyItemId: Int, learned: Boolean) {
        val index = studyItems.indexOfFirst { it.id == studyItemId }
        if (index >= 0) {
            studyItems[index] = studyItems[index].copy(learned = learned)
        }
    }

    override suspend fun decrementItemPriority(studyItem: StudyItem) {}

    override suspend fun onKnewIt(studyItem: StudyItem) {
        val index = studyItems.indexOfFirst { it.id == studyItem.id }
        if (index >= 0) {
            val newP = studyItem.maxPriority + 1
            studyItems[index] = studyItem.copy(currentPriority = newP, maxPriority = newP, learned = true)
        }
    }

    override suspend fun onForgot(studyItem: StudyItem) {
        val index = studyItems.indexOfFirst { it.id == studyItem.id }
        if (index >= 0) {
            val newMax = (studyItem.maxPriority - 1).coerceAtLeast(1)
            studyItems[index] = studyItem.copy(currentPriority = 1, maxPriority = newMax, learned = false)
        }
    }

    override fun searchCharactersByTokens(tokens: List<SearchToken>) = flowOf(characters)
    override fun searchWordsByTokens(tokens: List<SearchToken>) = flowOf(words)
    override fun clearCaches() {}

    override suspend fun getEnhancedCharacterInfo(characterId: Int): EnhancedCharacterInfo? = null
    override suspend fun saveOrUpdateCharacterDetail(detail: CharacterDetail): Long = 1L
}

class UseCaseTest {

    private lateinit var fakeRepository: FakeDictionaryRepository
    private lateinit var searchUseCase: SearchDictionaryUseCase
    private lateinit var processAnswerUseCase: ProcessReviewAnswerUseCase
    private lateinit var manageStudyListUseCase: ManageStudyListUseCase

    @Before
    fun setUp() {
        fakeRepository = FakeDictionaryRepository()
        searchUseCase = SearchDictionaryUseCase(fakeRepository)
        processAnswerUseCase = ProcessReviewAnswerUseCase(fakeRepository)
        manageStudyListUseCase = ManageStudyListUseCase(fakeRepository)
    }

    @Test
    fun testSearchDictionaryUseCaseDelegatesCorrectly() = runTest {
        fakeRepository.characters.add(Character(id = 1, traditional = "好", simplified = "好", pinyin = "hao3", definition = "good"))

        val tokens = listOf(SearchToken(TokenType.PINYIN_SEARCH, "hao3"))
        val result = searchUseCase.searchCharacters(tokens).first()

        assertEquals(1, result.size)
        assertEquals("好", result[0].simplified)
    }

    @Test
    fun testManageStudyListUseCaseToggleStudyStatus() = runTest {
        val charItem = DictionaryItem.CharacterItem(
            Character(id = 5, traditional = "水", simplified = "水", pinyin = "shui3", definition = "water")
        )

        manageStudyListUseCase.toggleStudyStatus(charItem, currentlyStudied = false)

        assertTrue(fakeRepository.studyItems.any { it.itemId == 5 && it.itemType == "character" })
    }

    @Test
    fun testProcessReviewAnswerUseCaseKnewIt() = runTest {
        val studyItem = StudyItem(id = 10, itemId = 1, itemType = "character", currentPriority = 1, maxPriority = 1, learned = false)
        fakeRepository.studyItems.add(studyItem)

        processAnswerUseCase.execute(studyItem, knewIt = true)

        val updated = fakeRepository.studyItems.first { it.id == 10 }
        assertTrue(updated.learned)
        assertEquals(2, updated.maxPriority)
    }

    @Test
    fun testProcessReviewAnswerUseCaseForgot() = runTest {
        val studyItem = StudyItem(id = 20, itemId = 2, itemType = "word", currentPriority = 3, maxPriority = 4, learned = true)
        fakeRepository.studyItems.add(studyItem)

        processAnswerUseCase.execute(studyItem, knewIt = false)

        val updated = fakeRepository.studyItems.first { it.id == 20 }
        assertFalse(updated.learned)
        assertEquals(1, updated.currentPriority)
        assertEquals(3, updated.maxPriority)
    }

    @Test
    fun testManageStudyListUseCaseMarkAsLearnedDisambiguatesTypes() = runTest {
        // Both character and word have ID = 5, but distinct types
        val charItem = StudyItem(id = 101, itemId = 5, itemType = "character", learned = false)
        val wordItem = StudyItem(id = 102, itemId = 5, itemType = "word", learned = false)
        fakeRepository.studyItems.addAll(listOf(charItem, wordItem))

        // Mark only the word as learned
        manageStudyListUseCase.markAsLearned(itemId = 5, itemType = "word", learned = true)

        val updatedWord = fakeRepository.studyItems.first { it.id == 102 }
        val updatedChar = fakeRepository.studyItems.first { it.id == 101 }

        assertTrue("El word debe marcarse como learned", updatedWord.learned)
        assertFalse("El character NO debe alterarse accidentalmente", updatedChar.learned)
    }
}
