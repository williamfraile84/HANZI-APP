package com.jukhg10.hanzimemo.data.database

import androidx.room.Embedded
import androidx.room.Relation
import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.Word
import com.jukhg10.hanzimemo.data.models.StudyItem

/**
 * Clase de transferencia de datos (POJO) relacional para optimizar las consultas de Room.
 * Permite resolver de manera interna las entidades secundarias mediante un JOIN por lotes,
 * eliminando por completo el antipatrón de consultas secuenciales N+1 en bucles for.
 */
data class StudyWithDetail(
    @Embedded 
    val studyItem: StudyItem,

    @Relation(
        parentColumn = "item_id",
        entityColumn = "id"
    )
    val character: Character?,

    @Relation(
        parentColumn = "item_id",
        entityColumn = "id"
    )
    val word: Word?
)