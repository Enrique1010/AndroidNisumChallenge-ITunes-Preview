package com.erapps.itunespreview.data.room

import androidx.room.TypeConverter
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()

    @TypeConverter
    fun suggestionModelToString(suggestionModel: SuggestionModel): String? {
        return gson.toJson(suggestionModel)
    }

    @TypeConverter
    fun stringToSuggestionModel(json: String): SuggestionModel? {
        return gson.fromJson(json, SuggestionModel::class.java)
    }

    @TypeConverter
    fun listSuggestionModelToString(suggestions: MutableList<SuggestionModel>): String? {
        return gson.toJson(suggestions)
    }

    @TypeConverter
    fun stringToListSuggestionModel(json: String): MutableList<SuggestionModel>? {
        //if (json.isEmpty()) return emptyList()

        val listType = object : TypeToken<MutableList<SuggestionModel>>() {}.type
        return gson.fromJson(json, listType)
    }
}