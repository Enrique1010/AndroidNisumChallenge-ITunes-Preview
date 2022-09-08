package com.erapps.itunespreview.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel

@Database(
    entities = [SuggestionModel::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ITunesPreviewDataBase: RoomDatabase() {
    abstract fun suggestionsDao(): SuggestionsDao
}