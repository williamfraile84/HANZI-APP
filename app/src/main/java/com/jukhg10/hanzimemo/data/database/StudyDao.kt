package com.jukhg10.hanzimemo.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import com.jukhg10.hanzimemo.data.models.StudyItem

@Dao
interface StudyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(item: StudyItem): Long

    @Query("DELETE FROM study_list WHERE item_id = :itemId AND item_type = :itemType")
    suspend fun removeItem(itemId: Int, itemType: String)

    @Query("SELECT * FROM study_list ORDER BY current_priority ASC, RANDOM()")
    suspend fun getStudyDeck(): List<StudyItem>

    @Query("SELECT * FROM study_list WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): StudyItem?

    @Query("SELECT * FROM study_list WHERE item_id = :itemId AND item_type = :itemType LIMIT 1")
    suspend fun getItemByItemId(itemId: Int, itemType: String): StudyItem?

    @Query("SELECT * FROM study_list ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomItems(limit: Int): List<StudyItem>

    @Query("SELECT * FROM study_list ORDER BY id DESC")
    fun getFullStudyList(): Flow<List<StudyItem>>

    /**
     * Recupera de forma reactiva la lista completa de ítems en estudio
     * resolviendo los detalles mediante JOIN relacional interno de Room sin problemas N+1.
     */
    @Transaction
    @Query("SELECT * FROM study_list ORDER BY id DESC")
    fun getFullStudyListWithDetails(): Flow<List<StudyWithDetail>>

    @Query("SELECT COUNT(*) > 0 FROM study_list WHERE item_id = :itemId AND item_type = :itemType")
    fun isBeingStudied(itemId: Int, itemType: String): Flow<Boolean>

    @Query("UPDATE study_list SET current_priority = :newCurrentPriority, max_priority = :newMaxPriority WHERE id = :itemId")
    suspend fun updateStudyProgress(itemId: Int, newCurrentPriority: Int, newMaxPriority: Int)

    @Query("UPDATE study_list SET current_priority = :newCurrentPriority WHERE id = :itemId")
    suspend fun updateCurrentPriority(itemId: Int, newCurrentPriority: Int)

    @Query("UPDATE study_list SET learned = :learned WHERE id = :itemId")
    suspend fun updateLearned(itemId: Int, learned: Boolean)
}