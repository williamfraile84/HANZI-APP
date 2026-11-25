package com.jukhg10.hanzimemo.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import com.jukhg10.hanzimemo.data.models.Character

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY pinyin ASC LIMIT 100")
    fun getAllCharacters(): Flow<List<Character>>

    @Query("""
        SELECT * FROM characters 
        WHERE simplified LIKE '%' || :query || '%' 
        OR traditional LIKE '%' || :query || '%' 
        OR REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(pinyin, '1', ''), '2', ''), '3', ''), '4', ''), '5', '') LIKE '%' || :query || '%' 
        OR definition LIKE '%' || :query || '%'
        ORDER BY
            /* Priority 1: Smart Exact Match for Pinyin */
            CASE 
                WHEN REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(pinyin, '1', ''), '2', ''), '3', ''), '4', ''), '5', '') = :query
                OR REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(pinyin, '1', ''), '2', ''), '3', ''), '4', ''), '5', '') LIKE :query || ' /%'
                OR REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(pinyin, '1', ''), '2', ''), '3', ''), '4', ''), '5', '') LIKE '%/ ' || :query || ' /%'
                OR REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(pinyin, '1', ''), '2', ''), '3', ''), '4', ''), '5', '') LIKE '%/ ' || :query
                THEN 0 
                ELSE 1 
            END,
            /* Priority 2: "Starts-with" match on any field */
            CASE
                WHEN REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(pinyin, '1', ''), '2', ''), '3', ''), '4', ''), '5', '') LIKE :query || '%' THEN 0
                ELSE 1
            END,
            /* Priority 3: Pinyin match over definition match */
            CASE
                WHEN REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(pinyin, '1', ''), '2', ''), '3', ''), '4', ''), '5', '') LIKE '%' || :query || '%' THEN 0
                ELSE 1
            END,
            /* Final tie-breakers */
            (SELECT CASE WHEN hsk_level > 0 THEN hsk_level ELSE 99 END FROM character_details WHERE character_id = characters.id) ASC,
            LENGTH(simplified) ASC,
            LOWER(pinyin) ASC
    """)
    fun searchCharacters(query: String): Flow<List<Character>>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): Character?

    /**
     * Búsqueda dinámica basada en tokens ejecutada en SQLite con semántica AND.
     * Evita la descarga de colecciones completas a la RAM.
     */
    @RawQuery(observedEntities = [Character::class])
    fun searchCharactersByTokens(query: SupportSQLiteQuery): Flow<List<Character>>
}