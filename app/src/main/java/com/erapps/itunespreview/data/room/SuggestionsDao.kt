package com.erapps.itunespreview.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel

@Dao
interface SuggestionsDao {

    @Query("select * from suggestions order by id desc")
    suspend fun getSuggestions(): MutableList<SuggestionModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestionModel: SuggestionModel)

    @Query("DELETE FROM suggestions where id NOT IN (SELECT id from suggestions ORDER BY id DESC LIMIT 7)")
    suspend fun clearSuggestions()

    @Query("DELETE FROM suggestions WHERE id NOT IN (SELECT MIN(id) FROM suggestions GROUP BY id, suggestion)")
    suspend fun deleteDuplicates()
}