package com.jukhg10.hanzimemo.domain.usecase

import com.jukhg10.hanzimemo.data.models.StudyItem
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import javax.inject.Inject

class ProcessReviewAnswerUseCase @Inject constructor(
    private val repository: IDictionaryRepository
) {
    suspend fun execute(studyItem: StudyItem, knewIt: Boolean) {
        if (knewIt) {
            repository.onKnewIt(studyItem)
        } else {
            repository.onForgot(studyItem)
        }
    }
}