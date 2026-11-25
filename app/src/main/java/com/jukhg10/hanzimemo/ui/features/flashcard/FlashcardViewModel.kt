package com.jukhg10.hanzimemo.ui.features.flashcard

import android.util.Log

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.jukhg10.hanzimemo.data.audio.TextToSpeechManager

import com.jukhg10.hanzimemo.data.repository.ReviewPair

import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo

import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository

import com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionEngine

import com.jukhg10.hanzimemo.domain.usecase.GetCharacterDetailUseCase

import com.jukhg10.hanzimemo.domain.usecase.GetReviewDeckUseCase

import com.jukhg10.hanzimemo.domain.usecase.ProcessReviewAnswerUseCase

import com.jukhg10.hanzimemo.ui.features.characters.DictionaryItem

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.*

import kotlinx.coroutines.launch

import javax.inject.Inject

enum class StudyStatus { STUDYING, LEARNED }

data class FlashcardUiState(

val deck: List<ReviewPair> = emptyList(),

val currentItem: ReviewPair? = null,

val detailDictionaryItem: DictionaryItem? = null,

val enhancedCharacterInfo: EnhancedCharacterInfo? = null,

val isCurrentItemStudied: Boolean = false,

val isLoadingDetail: Boolean = false,

val isRevealed: Boolean = false,

val sessionActive: Boolean = false,

val currentStudyStatus: StudyStatus = StudyStatus.STUDYING

)

@HiltViewModel

class FlashcardViewModel @Inject constructor(

private val getReviewDeckUseCase: GetReviewDeckUseCase,

private val processReviewAnswerUseCase: ProcessReviewAnswerUseCase,

private val getCharacterDetailUseCase: GetCharacterDetailUseCase,

private val repository: IDictionaryRepository,

val recognitionEngine: HandwritingRecognitionEngine,

private val textToSpeechManager: TextToSpeechManager

) : ViewModel() {

fun speak(text: String) {

textToSpeechManager.speak(text)

}

private val _uiState = MutableStateFlow(FlashcardUiState())

val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

private val _sessionDeck = MutableStateFlow<List<ReviewPair>>(emptyList())

private var studyingDeck: MutableList<ReviewPair> = mutableListOf()

private var learnedDeck: MutableList<ReviewPair> = mutableListOf()

private var currentIndex = -1

val characterCount: StateFlow<Int> = _sessionDeck.map { deck ->

deck.count { it.dictionaryItem is DictionaryItem.CharacterItem }

}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

val wordCount: StateFlow<Int> = _sessionDeck.map { deck ->

deck.count { it.dictionaryItem is DictionaryItem.WordItem }

}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

val studyingCount: StateFlow<Int> = _sessionDeck.map { deck ->

deck.count { !it.studyItem.learned }

}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

val learnedCount: StateFlow<Int> = _sessionDeck.map { deck ->

deck.count { it.studyItem.learned }

}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

val totalCount: StateFlow<Int> = _sessionDeck.map { deck ->

deck.size

}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

val progressPercentage: StateFlow<Float> = combine(learnedCount, totalCount) { learned, total ->

if (total == 0) 0f else (learned.toFloat() / total) * 100

}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

fun startSession() {

viewModelScope.launch {

val sessionList = getReviewDeckUseCase.getSessionDeck().toMutableList()

_sessionDeck.emit(sessionList)

studyingDeck = sessionList.filter { !it.studyItem.learned }.toMutableList()

learnedDeck = sessionList.filter { it.studyItem.learned }.toMutableList()

val currentStatus = _uiState.value.currentStudyStatus

val targetDeck = if (currentStatus == StudyStatus.STUDYING) studyingDeck else learnedDeck

_uiState.update {

it.copy(

deck = targetDeck,

sessionActive = sessionList.isNotEmpty(),

currentItem = null,

enhancedCharacterInfo = null,

currentStudyStatus = currentStatus

)

}

currentIndex = -1

if (targetDeck.isNotEmpty()) {

findNextCard()

}

}

}

fun loadItemById(studyItemId: Int) {

viewModelScope.launch {

try {

val item = getReviewDeckUseCase.getById(studyItemId)

if (item != null) {

val sessionList = getReviewDeckUseCase.getSessionDeck().toMutableList()

if (sessionList.none { it.studyItem.id == item.studyItem.id }) {

sessionList.add(item)

}

_sessionDeck.emit(sessionList)

studyingDeck = sessionList.filter { !it.studyItem.learned }.toMutableList()

learnedDeck = sessionList.filter { it.studyItem.learned }.toMutableList()

val targetStatus = if (item.studyItem.learned) StudyStatus.LEARNED else StudyStatus.STUDYING

val targetDeck = if (targetStatus == StudyStatus.STUDYING) studyingDeck else learnedDeck

val targetIndex = targetDeck.indexOfFirst { it.studyItem.id == item.studyItem.id }

currentIndex = if (targetIndex >= 0) targetIndex else 0

val currentPair = if (targetIndex >= 0) targetDeck[targetIndex] else item

_uiState.update {

it.copy(

deck = targetDeck,

sessionActive = true,

currentItem = currentPair,

isRevealed = false,

currentStudyStatus = targetStatus

)

}

loadEnhancedDetailForCurrentItem(currentPair)

} else {

startSession()

}

} catch (e: Exception) {

Log.e("FlashcardVM", "loadItemById error", e)

startSession()

}

}

}

fun loadDetailOnly(itemType: String, dictionaryId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingDetail = true,
                    detailDictionaryItem = null,
                    enhancedCharacterInfo = null,
                    currentItem = null
                )
            }
            try {
                var dictItem: DictionaryItem? = null
                var enhanced: EnhancedCharacterInfo?

if (itemType == "character") {
                    enhanced = getCharacterDetailUseCase.execute(dictionaryId)
                    val char = repository.getCharacterById(dictionaryId)
                    if (char != null) {
                        dictItem = DictionaryItem.CharacterItem(char)
                        if (enhanced == null) {
                            enhanced = repository.getEnhancedCharacterInfo(dictionaryId)
                        }
                    }
                } else {
                    val word = repository.getWordById(dictionaryId)
                    if (word != null) {
                        dictItem = DictionaryItem.WordItem(word)
                    }
                    enhanced = null
                }

val isStudied = repository.isItemBeingStudied(dictionaryId, itemType).first()

_uiState.update {
                    it.copy(
                        detailDictionaryItem = dictItem,
                        enhancedCharacterInfo = enhanced,
                        isCurrentItemStudied = isStudied,
                        isLoadingDetail = false,
                        isRevealed = true
                    )
                }
            } catch (e: Exception) {
                Log.e("FlashcardVM", "loadDetailOnly error", e)
                _uiState.update { it.copy(isLoadingDetail = false) }
            }
        }
    }

fun toggleDetailStudyStatus(itemType: String, dictionaryId: Int) {

viewModelScope.launch {

val currentlyStudied = _uiState.value.isCurrentItemStudied

if (currentlyStudied) {

repository.removeItemFromStudyList(dictionaryId, itemType)

_uiState.update { it.copy(isCurrentItemStudied = false) }

} else {

repository.addItemToStudyList(com.jukhg10.hanzimemo.data.models.StudyItem(itemId = dictionaryId, itemType = itemType))

_uiState.update { it.copy(isCurrentItemStudied = true) }

}

}

}

fun loadByDictionaryId(itemType: String, dictionaryId: Int) {

loadDetailOnly(itemType, dictionaryId)

}

private fun loadEnhancedDetailForCurrentItem(pair: ReviewPair?) {

if (pair == null) return

viewModelScope.launch {

_uiState.update { it.copy(isLoadingDetail = true) }

val dictItem = pair.dictionaryItem

if (dictItem is DictionaryItem.CharacterItem) {

val info = getCharacterDetailUseCase.execute(dictItem.character.id)

_uiState.update { it.copy(enhancedCharacterInfo = info, isLoadingDetail = false) }

} else {

_uiState.update { it.copy(enhancedCharacterInfo = null, isLoadingDetail = false) }

}

}

}

private fun findNextCard() {

viewModelScope.launch {

val currentDeck = if (_uiState.value.currentStudyStatus == StudyStatus.STUDYING) studyingDeck else learnedDeck

if (currentDeck.isEmpty()) {

_uiState.update { it.copy(currentItem = null, enhancedCharacterInfo = null, isRevealed = false, deck = emptyList()) }

return@launch

}

var searchIndex = currentIndex

var cardFound = false

for (i in 0 until currentDeck.size) {

searchIndex = (searchIndex + 1) % currentDeck.size

val currentPair = currentDeck[searchIndex]

if (currentPair.studyItem.currentPriority > 1) {

val newPriority = currentPair.studyItem.currentPriority - 1

repository.decrementItemPriority(currentPair.studyItem)

currentDeck[searchIndex] = currentPair.copy(

studyItem = currentPair.studyItem.copy(currentPriority = newPriority)

)

} else {

currentIndex = searchIndex

_uiState.update {

it.copy(

deck = currentDeck,

currentItem = currentPair,

isRevealed = false

)

}

cardFound = true

loadEnhancedDetailForCurrentItem(currentPair)

break

}

}

if (!cardFound && currentDeck.isNotEmpty()) {

currentIndex = 0

val selectedPair = currentDeck[0]

_uiState.update {

it.copy(

deck = currentDeck,

currentItem = selectedPair,

isRevealed = false

)

}

loadEnhancedDetailForCurrentItem(selectedPair)

}

}

}

fun handleReviewAction(knewIt: Boolean) {

val currentPair = _uiState.value.currentItem ?: return

viewModelScope.launch {

try {

processReviewAnswerUseCase.execute(currentPair.studyItem, knewIt)

if (knewIt) {

val newMaxPriority = currentPair.studyItem.maxPriority + 1

val updatedStudyItem = currentPair.studyItem.copy(

currentPriority = newMaxPriority,

maxPriority = newMaxPriority,

learned = true

)

val updatedPair = currentPair.copy(studyItem = updatedStudyItem)

val currentDeck = if (_uiState.value.currentStudyStatus == StudyStatus.STUDYING) studyingDeck else learnedDeck

val indexInCurrentDeck = currentDeck.indexOfFirst { it.studyItem.id == currentPair.studyItem.id }

if (indexInCurrentDeck >= 0) {

currentDeck.removeAt(indexInCurrentDeck)

}

if (learnedDeck.none { it.studyItem.id == updatedPair.studyItem.id }) {

learnedDeck.add(updatedPair)

}

val indexInSession = _sessionDeck.value.indexOfFirst { it.studyItem.id == currentPair.studyItem.id }

if (indexInSession >= 0) {

val updatedSession = _sessionDeck.value.toMutableList()

updatedSession[indexInSession] = updatedPair

_sessionDeck.emit(updatedSession)

}

val targetDeck = if (_uiState.value.currentStudyStatus == StudyStatus.STUDYING) studyingDeck else learnedDeck

currentIndex = -1 

if (targetDeck.isNotEmpty()) {

findNextCard()

} else {

_uiState.update { it.copy(deck = emptyList(), currentItem = null, enhancedCharacterInfo = null, isRevealed = false) }

}

} else {

val newCurrentPriority = 1

val newMaxPriority = (currentPair.studyItem.maxPriority - 1).coerceAtLeast(1)

val updatedStudyItem = currentPair.studyItem.copy(

currentPriority = newCurrentPriority,

maxPriority = newMaxPriority,

learned = false

)

val updatedPair = currentPair.copy(studyItem = updatedStudyItem)

val indexInLearned = learnedDeck.indexOfFirst { it.studyItem.id == currentPair.studyItem.id }

if (indexInLearned >= 0) {

learnedDeck.removeAt(indexInLearned)

}

if (studyingDeck.none { it.studyItem.id == updatedPair.studyItem.id }) {

studyingDeck.add(updatedPair)

}

val indexInSession = _sessionDeck.value.indexOfFirst { it.studyItem.id == currentPair.studyItem.id }

if (indexInSession >= 0) {

val updatedSession = _sessionDeck.value.toMutableList()

updatedSession[indexInSession] = updatedPair

_sessionDeck.emit(updatedSession)

}

val targetDeck = if (_uiState.value.currentStudyStatus == StudyStatus.STUDYING) studyingDeck else learnedDeck

currentIndex = -1

if (targetDeck.isNotEmpty()) {

findNextCard()

} else {

_uiState.update { it.copy(deck = emptyList(), currentItem = null, enhancedCharacterInfo = null, isRevealed = false) }

}

}

} catch (e: Exception) {

Log.e("HanziMemo.VM", "handleReviewAction failed: ${e.message}", e)

}

}

}

fun proceedToNextItem() {

findNextCard()

}

fun showNextItem() {

findNextCard()

}

fun revealCard() {

_uiState.update { it.copy(isRevealed = true) }

}

fun toggleRevealCard() {

_uiState.update { it.copy(isRevealed = !it.isRevealed) }

}

fun showPreviousItem() {

viewModelScope.launch {

val currentDeck = if (_uiState.value.currentStudyStatus == StudyStatus.STUDYING) studyingDeck else learnedDeck

if (currentDeck.isEmpty()) return@launch

val newIndex = if (currentIndex - 1 < 0) currentDeck.size - 1 else currentIndex - 1

if (newIndex in currentDeck.indices) {

currentIndex = newIndex

val previousPair = currentDeck[newIndex]

_uiState.update {

it.copy(

deck = currentDeck,

currentItem = previousPair,

isRevealed = false

)

}

loadEnhancedDetailForCurrentItem(previousPair)

}

}

}

fun syncDeckOnResume() {

viewModelScope.launch {

val currentDbDeck = getReviewDeckUseCase.getSessionDeck()

val deckInMemoryIds = _sessionDeck.value.map { it.studyItem.id }.toSet()

val dbDeckIds = currentDbDeck.map { it.studyItem.id }.toSet()

if (deckInMemoryIds != dbDeckIds || _sessionDeck.value.isEmpty()) {

startSession()

}

}

}

fun switchStudyStatus(newStatus: StudyStatus) {

viewModelScope.launch {

val targetDeck = if (newStatus == StudyStatus.STUDYING) studyingDeck else learnedDeck

val nextItem = if (targetDeck.isNotEmpty()) targetDeck[0] else null

_uiState.update {

it.copy(

deck = targetDeck,

currentStudyStatus = newStatus,

currentItem = nextItem,

isRevealed = false

)

}

currentIndex = -1

if (targetDeck.isNotEmpty()) {

findNextCard()

}

}

}

}