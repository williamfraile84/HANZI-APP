package com.jukhg10.hanzimemo.ui.features.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jukhg10.hanzimemo.data.HanziRecognitionService
import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.StudyItem
import com.jukhg10.hanzimemo.data.models.Word
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import com.jukhg10.hanzimemo.domain.search.SearchToken
import com.jukhg10.hanzimemo.domain.search.TokenType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HanziMatch(
    val hanzi: String,
    val confidence: Float,
    val matchedCharacter: Character? = null,
    val matchedWord: Word? = null,
    val isInStudyList: Boolean = false
)

data class CameraRecognitionUiState(
    val isLoading: Boolean = false,
    val matches: List<HanziMatch> = emptyList(),
    val error: String? = null,
    val lastRecognizedText: String = "",
    val hasProcessedImage: Boolean = false
)

@HiltViewModel
class CameraRecognitionViewModel @Inject constructor(
    private val repository: IDictionaryRepository,
    private val recognitionService: HanziRecognitionService
) : ViewModel() {
    private val _uiState = MutableStateFlow(CameraRecognitionUiState())
    val uiState: StateFlow<CameraRecognitionUiState> = _uiState.asStateFlow()

    fun recognizeFromUri(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, hasProcessedImage = true)
            try {
                val recognizedText = recognitionService.recognizeTextFromUri(uri)?.trim() ?: ""
                val matches = mutableListOf<HanziMatch>()
                val charactersToMatch = recognizedText.filter { !it.isWhitespace() }

                for (ch in charactersToMatch) {
                    val hanziStr = ch.toString()
                    val tokenList = listOf(SearchToken(TokenType.HANZI_SEARCH, hanziStr))

                    val characterList = repository.searchCharactersByTokens(tokenList).first()
                    val matchedChar = characterList.firstOrNull {
                        it.simplified == hanziStr || it.traditional == hanziStr
                    }

                    val wordList = if (matchedChar == null) {
                        repository.searchWordsByTokens(tokenList).first()
                    } else emptyList()

                    val matchedWord = wordList.firstOrNull {
                        it.simplified == hanziStr || it.traditional == hanziStr
                    }

                    if (matchedChar != null) {
                        val isStudying = repository.isItemBeingStudied(matchedChar.id, "character").first()
                        matches.add(
                            HanziMatch(
                                hanzi = hanziStr,
                                confidence = 0.95f,
                                matchedCharacter = matchedChar,
                                matchedWord = null,
                                isInStudyList = isStudying
                            )
                        )
                    } else if (matchedWord != null) {
                        val isStudying = repository.isItemBeingStudied(matchedWord.id, "word").first()
                        matches.add(
                            HanziMatch(
                                hanzi = hanziStr,
                                confidence = 0.95f,
                                matchedCharacter = null,
                                matchedWord = matchedWord,
                                isInStudyList = isStudying
                            )
                        )
                    } else {
                        matches.add(
                            HanziMatch(
                                hanzi = hanziStr,
                                confidence = 0.50f,
                                matchedCharacter = null,
                                matchedWord = null,
                                isInStudyList = false
                            )
                        )
                    }
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    matches = matches,
                    lastRecognizedText = recognizedText,
                    hasProcessedImage = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al reconocer imagen: ${e.message ?: "Desconocido"}",
                    hasProcessedImage = true
                )
            }
        }
    }

    fun addMatchToStudyList(match: HanziMatch) {
        viewModelScope.launch {
            val id = match.matchedCharacter?.id ?: match.matchedWord?.id
            val type = if (match.matchedCharacter != null) "character" else "word"
            if (id != null) {
                repository.addItemToStudyList(StudyItem(itemId = id, itemType = type))
                _uiState.value = _uiState.value.copy(
                    matches = _uiState.value.matches.map {
                        if (it.hanzi == match.hanzi) it.copy(isInStudyList = true) else it
                    }
                )
            }
        }
    }

    fun removeMatchFromStudyList(match: HanziMatch) {
        viewModelScope.launch {
            val id = match.matchedCharacter?.id ?: match.matchedWord?.id
            val type = if (match.matchedCharacter != null) "character" else "word"
            if (id != null) {
                repository.removeItemFromStudyList(id, type)
                _uiState.value = _uiState.value.copy(
                    matches = _uiState.value.matches.map {
                        if (it.hanzi == match.hanzi) it.copy(isInStudyList = false) else it
                    }
                )
            }
        }
    }

    fun clearMatches() {
        _uiState.value = CameraRecognitionUiState()
    }

    fun setError(message: String?) {
        _uiState.value = _uiState.value.copy(error = message)
    }
}

