package com.jukhg10.hanzimemo.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jukhg10.hanzimemo.data.models.CharacterDetail

@Dao
interface CharacterDetailDao {
    @Query("SELECT * FROM character_details WHERE character_id = :characterId LIMIT 1")
    suspend fun getDetailByCharacterId(characterId: Int): CharacterDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDetail(detail: CharacterDetail): Long

    @Query("SELECT COUNT(*) FROM character_details")
    suspend fun getDetailCount(): Int
}