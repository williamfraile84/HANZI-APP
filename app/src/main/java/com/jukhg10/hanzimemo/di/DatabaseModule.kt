package com.jukhg10.hanzimemo.di

import android.content.Context
import com.jukhg10.hanzimemo.data.database.AppDatabase
import com.jukhg10.hanzimemo.data.database.CharacterDao
import com.jukhg10.hanzimemo.data.database.CharacterDetailDao
import com.jukhg10.hanzimemo.data.database.StudyDao
import com.jukhg10.hanzimemo.data.database.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideCharacterDao(database: AppDatabase): CharacterDao {
        return database.characterDao()
    }

    @Provides
    fun provideWordDao(database: AppDatabase): WordDao {
        return database.wordDao()
    }

    @Provides
    fun provideStudyDao(database: AppDatabase): StudyDao {
        return database.studyDao()
    }

    @Provides
    fun provideCharacterDetailDao(database: AppDatabase): CharacterDetailDao {
        return database.characterDetailDao()
    }
}