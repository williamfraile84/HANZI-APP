package com.jukhg10.hanzimemo.ui.features.review

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jukhg10.hanzimemo.data.repository.ReviewPair
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import com.jukhg10.hanzimemo.domain.usecase.GetReviewDeckUseCase
import com.jukhg10.hanzimemo.domain.usecase.ManageStudyListUseCase
import com.jukhg10.hanzimemo.ui.features.characters.DictionaryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getReviewDeckUseCase: GetReviewDeckUseCase,
    private val manageStudyListUseCase: ManageStudyListUseCase,
    private val repository: IDictionaryRepository
) : ViewModel() {

    companion object {
        private val TONE_DIGITS_REGEX = "[12345]".toRegex()
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val allReviewItems: StateFlow<List<ReviewPair>> = 
        getReviewDeckUseCase.getFullListWithStatus()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val studyingItems: StateFlow<List<DictionaryItem>> =
        allReviewItems
            .combine(_searchQuery) { list, query ->
                list
                    .filter { !it.studyItem.learned }
                    .map { it.dictionaryItem }
                    .filter { item ->
                        if (query.isBlank()) true else matchesQuery(item, query)
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val learnedItems: StateFlow<List<DictionaryItem>> =
        allReviewItems
            .combine(_searchQuery) { list, query ->
                list
                    .filter { it.studyItem.learned }
                    .map { it.dictionaryItem }
                    .filter { item ->
                        if (query.isBlank()) true else matchesQuery(item, query)
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val reviewItems: StateFlow<List<DictionaryItem>> =
        allReviewItems
            .combine(_searchQuery) { list, query ->
                list
                    .map { it.dictionaryItem }
                    .filter { item ->
                        if (query.isBlank()) true else matchesQuery(item, query)
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val characterCount: StateFlow<Int> = reviewItems.map { list ->
        list.count { it is DictionaryItem.CharacterItem }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val wordCount: StateFlow<Int> = reviewItems.map { list ->
        list.count { it is DictionaryItem.WordItem }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val learnedCount: StateFlow<Int> = learnedItems.map { it.size }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalCount: StateFlow<Int> = reviewItems.map { it.size }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val progressPercentage: StateFlow<Float> = 
        combine(learnedCount, totalCount) { learned, total ->
            if (total == 0) 0f else (learned.toFloat() / total) * 100f
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0f
        )

    private fun matchesQuery(item: DictionaryItem, query: String): Boolean {
        return when (item) {
            is DictionaryItem.CharacterItem -> {
                val char = item.character
                char.simplified.contains(query, ignoreCase = true) ||
                        char.traditional.contains(query, ignoreCase = true) ||
                        char.pinyin.replace(TONE_DIGITS_REGEX, "").contains(query, ignoreCase = true) ||
                        char.definition.contains(query, ignoreCase = true)
            }
            is DictionaryItem.WordItem -> {
                val word = item.word
                word.simplified.contains(query, ignoreCase = true) ||
                        word.traditional.contains(query, ignoreCase = true) ||
                        word.pinyin.replace(TONE_DIGITS_REGEX, "").contains(query, ignoreCase = true) ||
                        word.definition.contains(query, ignoreCase = true)
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun removeItem(item: DictionaryItem) {
        viewModelScope.launch {
            val (itemId, itemType) = when (item) {
                is DictionaryItem.CharacterItem -> Pair(item.character.id, "character")
                is DictionaryItem.WordItem -> Pair(item.word.id, "word")
            }
            repository.removeItemFromStudyList(itemId, itemType)
        }
    }

    fun markAsLearned(itemId: Int, itemType: String, learned: Boolean) {
        viewModelScope.launch {
            try {
                manageStudyListUseCase.markAsLearned(itemId, itemType, learned)
            } catch (e: Exception) {
                Log.e("HanziMemo.ReviewVM", "markAsLearned failed: ${e.message}", e)
            }
        }
    }
}