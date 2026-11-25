package com.jukhg10.hanzimemo.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_list",
    indices = [
        // Índice único compuesto para evitar duplicidad de elementos del mismo tipo
        Index(value = ["item_id", "item_type"], unique = true),
        // Índices individuales de filtrado frecuente para mejorar consultas del algoritmo SRS
        Index(value = ["learned"]),
        Index(value = ["item_type"]),
        Index(value = ["current_priority"])
    ]
)
data class StudyItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "item_id")
    val itemId: Int,

    @ColumnInfo(name = "item_type")
    val itemType: String,

    @ColumnInfo(name = "current_priority", defaultValue = "1")
    val currentPriority: Int = 1,

    @ColumnInfo(name = "max_priority", defaultValue = "1")
    val maxPriority: Int = 1,

    @ColumnInfo(name = "learned", defaultValue = "0")
    val learned: Boolean = false
)