package com.jukhg10.hanzimemo.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad de Room que almacena la riqueza pedagógica e instructiva de cada carácter.
 * Mantiene compatibilidad y relación relacional con la tabla [Character].
 */
@Entity(
    tableName = "character_details",
    foreignKeys = [
        ForeignKey(
            entity = Character::class,
            parentColumns = ["id"],
            childColumns = ["character_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["character_id"], unique = true),
        Index(value = ["hsk_level"]),
        Index(value = ["radical"])
    ]
)
data class CharacterDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "character_id")
    val characterId: Int,

    @ColumnInfo(name = "hsk_level", defaultValue = "1")
    val hskLevel: Int = 1,

    @ColumnInfo(name = "radical")
    val radical: String = "",

    @ColumnInfo(name = "stroke_count", defaultValue = "0")
    val strokeCount: Int = 0,

    @ColumnInfo(name = "stroke_order_data", defaultValue = "")
    val strokeOrderData: String = "",

    @ColumnInfo(name = "illustration_res_name", defaultValue = "")
    val illustrationResName: String? = null,

    @ColumnInfo(name = "components_json", defaultValue = "[]")
    val componentsJson: String = "[]",

    @ColumnInfo(name = "etymology_origin", defaultValue = "")
    val etymologyOrigin: String = "",

    @ColumnInfo(name = "historical_evolution", defaultValue = "")
    val historicalEvolution: String = "",

    @ColumnInfo(name = "mnemonic_story", defaultValue = "")
    val mnemonicStory: String = "",

    @ColumnInfo(name = "illustration_explanation", defaultValue = "")
    val illustrationExplanation: String = "",

    @ColumnInfo(name = "secondary_meanings", defaultValue = "")
    val secondaryMeanings: String = "",

    @ColumnInfo(name = "frequent_words_json", defaultValue = "[]")
    val frequentWordsJson: String = "[]",

    @ColumnInfo(name = "example_sentences_json", defaultValue = "[]")
    val exampleSentencesJson: String = "[]",

    @ColumnInfo(name = "cultural_notes", defaultValue = "")
    val culturalNotes: String? = null
)