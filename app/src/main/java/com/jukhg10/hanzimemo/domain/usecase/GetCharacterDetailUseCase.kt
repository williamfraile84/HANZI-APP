package com.jukhg10.hanzimemo.domain.usecase

import com.jukhg10.hanzimemo.domain.models.EnhancedCharacterInfo
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import javax.inject.Inject

/**
 * Caso de uso encargado de recuperar los detalles visuales, mnemotécnicos
 * y lingüísticos de un carácter específico.
 */
class GetCharacterDetailUseCase @Inject constructor(
    private val repository: IDictionaryRepository
) {
    suspend fun execute(characterId: Int): EnhancedCharacterInfo? {
        return repository.getEnhancedCharacterInfo(characterId)
    }
}