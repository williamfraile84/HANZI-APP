package com.jukhg10.hanzimemo.domain.usecase

import com.jukhg10.hanzimemo.data.repository.ReviewPair
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewDeckUseCase @Inject constructor(
    private val repository: IDictionaryRepository
) {
    suspend fun getSessionDeck(): List<ReviewPair> {
        return repository.getReviewDeck()
    }

    fun getFullListWithStatus(): Flow<List<ReviewPair>> {
        return repository.getFullStudyListWithStatus()
    }

    suspend fun getById(studyItemId: Int): ReviewPair? {
        return repository.getStudyItemById(studyItemId)
    }

    suspend fun findOrCreateByDictId(itemId: Int, itemType: String): ReviewPair? {
        val studyItem = repository.findOrCreateStudyItem(itemId, itemType) ?: return null
        return repository.getStudyItemById(studyItem.id)
    }
}