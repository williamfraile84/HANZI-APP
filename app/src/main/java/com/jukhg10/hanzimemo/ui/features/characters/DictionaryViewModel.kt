package com.jukhg10.hanzimemo.ui.features.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.Word
import com.jukhg10.hanzimemo.domain.search.SearchLexer
import com.jukhg10.hanzimemo.domain.search.SearchToken
import com.jukhg10.hanzimemo.domain.usecase.ManageStudyListUseCase
import com.jukhg10.hanzimemo.domain.usecase.SearchDictionaryUseCase
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DictionaryMode { CHARACTERS, WORDS }

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val searchDictionaryUseCase: SearchDictionaryUseCase,
    private val manageStudyListUseCase: ManageStudyListUseCase,
    private val repository: IDictionaryRepository,
    val recognitionEngine: HandwritingRecognitionEngine
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchTokens = MutableStateFlow<List<SearchToken>>(emptyList())
    val searchTokens: StateFlow<List<SearchToken>> = _searchTokens.asStateFlow()

    private val _mode = MutableStateFlow(DictionaryMode.CHARACTERS)
    val mode: StateFlow<DictionaryMode> = _mode.asStateFlow()

    private val _isQueryValid = MutableStateFlow(true)
    val isQueryValid: StateFlow<Boolean> = _isQueryValid.asStateFlow()

    val studiedItemKeys: StateFlow<Set<String>> = repository.getFullStudyListItems()
        .map { list ->
            list.map { item ->
                when (item) {
                    is DictionaryItem.CharacterItem -> "character_${item.character.id}"
                    is DictionaryItem.WordItem -> "word_${item.word.id}"
                }
            }.toSet()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    val items: StateFlow<List<DictionaryItem>> = combine(
        _mode,
        _searchQuery.debounce(200).distinctUntilChanged()
    ) { currentMode, query ->
        val tokens = if (query.isBlank()) emptyList() else SearchLexer.lex(query)
        Pair(currentMode, tokens)
    }.flatMapLatest { (currentMode, tokens) ->
        when (currentMode) {
            DictionaryMode.CHARACTERS -> {
                searchDictionaryUseCase.searchCharacters(tokens)
                    .map { list -> list.map { DictionaryItem.CharacterItem(it) } }
            }
            DictionaryMode.WORDS -> {
                searchDictionaryUseCase.searchWords(tokens)
                    .map { list -> list.map { DictionaryItem.WordItem(it) } }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isBlank()) {
            _searchTokens.value = emptyList()
            _isQueryValid.value = true
        } else {
            _searchTokens.value = SearchLexer.lex(newQuery)
            _isQueryValid.value = true
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        onSearchQueryChanged(newQuery)
    }

    fun setMode(newMode: DictionaryMode) {
        _mode.value = newMode
    }

    fun isItemBeingStudied(item: DictionaryItem): Flow<Boolean> {
        val (id, type) = when (item) {
            is DictionaryItem.CharacterItem -> Pair(item.character.id, "character")
            is DictionaryItem.WordItem -> Pair(item.word.id, "word")
        }
        return manageStudyListUseCase.isItemBeingStudied(id, type)
    }

    fun addOrRemoveItem(item: DictionaryItem, isBeingStudied: Boolean) {
        viewModelScope.launch {
            manageStudyListUseCase.toggleStudyStatus(item, isBeingStudied)
        }
    }

    fun onTokensChanged(newTokens: List<SearchToken>) {
        _searchTokens.value = newTokens
    }

    fun addToken(token: SearchToken) {
        val current = _searchTokens.value.toMutableList()
        if (!current.contains(token)) {
            current.add(token)
            _searchTokens.value = current
        }
    }

    fun removeToken(token: SearchToken) {
        val current = _searchTokens.value.toMutableList()
        if (current.remove(token)) {
            _searchTokens.value = current
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchTokens.value = emptyList()
    }

    fun onLowMemoryDetected() {
        repository.clearCaches()
    }
}