package com.jukhg10.hanzimemo.domain.usecase

import com.jukhg10.hanzimemo.data.models.StudyItem
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import com.jukhg10.hanzimemo.ui.features.characters.DictionaryItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageStudyListUseCase @Inject constructor(
    private val repository: IDictionaryRepository
) {
    fun isItemBeingStudied(itemId: Int, itemType: String): Flow<Boolean> {
        return repository.isItemBeingStudied(itemId, itemType)
    }

    suspend fun toggleStudyStatus(item: DictionaryItem, currentlyStudied: Boolean) {
        val (itemId, itemType) = when (item) {
            is DictionaryItem.CharacterItem -> Pair(item.character.id, "character")
            is DictionaryItem.WordItem -> Pair(item.word.id, "word")
        }

        if (currentlyStudied) {
            repository.removeItemFromStudyList(itemId, itemType)
        } else {
            repository.addItemToStudyList(StudyItem(itemId = itemId, itemType = itemType))
        }
    }

    suspend fun markAsLearned(itemId: Int, itemType: String, learned: Boolean) {
        val studyItem = repository.getStudyItemByDictionaryId(itemId, itemType)
            ?: repository.findOrCreateStudyItem(itemId, itemType)

        studyItem?.let {
            repository.markItemAsLearned(it.id, learned)
        }
    }
}
