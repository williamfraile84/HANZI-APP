package com.jukhg10.hanzimemo.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jukhg10.hanzimemo.data.models.Character
import com.jukhg10.hanzimemo.data.models.CharacterDetail
import com.jukhg10.hanzimemo.data.models.Word
import com.jukhg10.hanzimemo.data.models.StudyItem

@Database(
    entities = [Character::class, Word::class, StudyItem::class, CharacterDetail::class], 
    version = 6, 
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun wordDao(): WordDao
    abstract fun studyDao(): StudyDao
    abstract fun characterDetailDao(): CharacterDetailDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // MIGRACIÓN 3 → 4: Agregar índices iniciales en caracteres y palabras
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                Log.d("AppDatabase", "🔧 Ejecutando migración 3 → 4: Creando índices en diccionario...")
                try {
                    db.execSQL("CREATE INDEX IF NOT EXISTS idx_character_pinyin ON characters(pinyin)")
                    db.execSQL("CREATE INDEX IF NOT EXISTS idx_word_pinyin ON words(pinyin)")
                    db.execSQL("CREATE INDEX IF NOT EXISTS idx_character_definition ON characters(definition)")
                    db.execSQL("CREATE INDEX IF NOT EXISTS idx_word_definition ON words(definition)")
                    db.execSQL("CREATE INDEX IF NOT EXISTS idx_study_learned ON study_list(learned)")
                    db.execSQL("CREATE INDEX IF NOT EXISTS idx_study_type ON study_list(item_type)")
                    Log.d("AppDatabase", "✅ Migración 3 → 4 completada")
                } catch (e: Exception) {
                    Log.e("AppDatabase", "❌ Error en migración 3 -> 4: ${e.message}", e)
                    throw e
                }
            }
        }

        // MIGRACIÓN 4 → 5: Optimización de esquema e índice compuesto en study_list
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                Log.d("AppDatabase", "🔧 Ejecutando migración 4 → 5: Creando índice compuesto único...")
                try {
                    db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_study_list_item_id_item_type ON study_list(item_id, item_type)")
                    db.execSQL("CREATE INDEX IF NOT EXISTS index_study_list_current_priority ON study_list(current_priority)")
                    Log.d("AppDatabase", "✅ Migración 4 → 5 completada")
                } catch (e: Exception) {
                    Log.e("AppDatabase", "❌ Error en migración 4 -> 5: ${e.message}", e)
                    throw e
                }
            }
        }

        // MIGRACIÓN 5 → 6: Creación de la tabla character_details para almacenar la información mnemotécnica e ilustraciones
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                Log.d("AppDatabase", "🔧 Ejecutando migración 5 → 6: Creando tabla de detalles mnemotécnicos e ilustraciones...")
                try {
                    db.execSQL("""
                        CREATE TABLE IF NOT EXISTS `character_details` (
                            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            `character_id` INTEGER NOT NULL,
                            `hsk_level` INTEGER NOT NULL DEFAULT 1,
                            `radical` TEXT NOT NULL DEFAULT '',
                            `stroke_count` INTEGER NOT NULL DEFAULT 0,
                            `stroke_order_data` TEXT NOT NULL DEFAULT '',
                            `illustration_res_name` TEXT,
                            `components_json` TEXT NOT NULL DEFAULT '[]',
                            `etymology_origin` TEXT NOT NULL DEFAULT '',
                            `historical_evolution` TEXT NOT NULL DEFAULT '',
                            `mnemonic_story` TEXT NOT NULL DEFAULT '',
                            `illustration_explanation` TEXT NOT NULL DEFAULT '',
                            `secondary_meanings` TEXT NOT NULL DEFAULT '',
                            `frequent_words_json` TEXT NOT NULL DEFAULT '[]',
                            `example_sentences_json` TEXT NOT NULL DEFAULT '[]',
                            `cultural_notes` TEXT,
                            FOREIGN KEY(`character_id`) REFERENCES `characters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                        )
                    """.trimIndent())
                    
                    db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_character_details_character_id` ON `character_details` (`character_id`)")
                    db.execSQL("CREATE INDEX IF NOT EXISTS `index_character_details_hsk_level` ON `character_details` (`hsk_level`)")
                    db.execSQL("CREATE INDEX IF NOT EXISTS `index_character_details_radical` ON `character_details` (`radical`)")
                    Log.d("AppDatabase", "✅ Migración 5 → 6 completada exitosamente")
                } catch (e: Exception) {
                    Log.e("AppDatabase", "❌ Error en migración 5 -> 6: ${e.message}", e)
                    throw e
                }
            }
        }

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "hanzi.db"
                    )
                        .createFromAsset("hanzi.db")
                        .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                        .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                                db.execSQL("PRAGMA foreign_keys = ON;")
                            }
                        })
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}