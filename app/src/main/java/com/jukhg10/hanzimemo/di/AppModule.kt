package com.jukhg10.hanzimemo.di

import android.content.Context
import com.jukhg10.hanzimemo.data.HanziRecognitionService
import com.jukhg10.hanzimemo.data.handwriting.HandwritingRecognitionEngine
import com.jukhg10.hanzimemo.data.audio.TextToSpeechManager
import com.jukhg10.hanzimemo.data.illustrations.IllustrationEngine
import com.jukhg10.hanzimemo.data.repository.DictionaryRepository
import com.jukhg10.hanzimemo.data.preferences.UserPreferencesManager
import com.jukhg10.hanzimemo.domain.repository.IDictionaryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDictionaryRepository(
        impl: DictionaryRepository
    ): IDictionaryRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    fun provideHanziRecognitionService(
        @ApplicationContext context: Context
    ): HanziRecognitionService {
        return HanziRecognitionService(context)
    }

    @Provides
    @Singleton
    fun provideIllustrationEngine(
        @ApplicationContext context: Context
    ): IllustrationEngine {
        return IllustrationEngine(context)
    }

    @Provides
    @Singleton
    fun provideTextToSpeechManager(
        @ApplicationContext context: Context
    ): TextToSpeechManager {
        return TextToSpeechManager(context)
    }

    @Provides
    @Singleton
    fun provideHandwritingRecognitionEngine(
        recognitionService: HanziRecognitionService
    ): HandwritingRecognitionEngine {
        return HandwritingRecognitionEngine(recognitionService)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesManager(
        @ApplicationContext context: Context
    ): UserPreferencesManager {
        return UserPreferencesManager.getInstance(context)
    }
}