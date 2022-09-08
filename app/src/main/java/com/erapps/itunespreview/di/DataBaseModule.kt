package com.erapps.itunespreview.di

import android.content.Context
import androidx.room.Room
import com.erapps.itunespreview.data.room.ITunesPreviewDataBase
import com.erapps.itunespreview.data.room.SuggestionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideSuggestionsDao(iTunesPreviewDataBase: ITunesPreviewDataBase): SuggestionsDao {
        return iTunesPreviewDataBase.suggestionsDao()
    }

    @Singleton
    @Provides
    fun provideITunesPreviewDataBase(@ApplicationContext appContext: Context): ITunesPreviewDataBase {
        return Room.databaseBuilder(
            appContext,
            ITunesPreviewDataBase::class.java,
            "ItunesPreview_DB"
        ).fallbackToDestructiveMigration().build()
    }
}