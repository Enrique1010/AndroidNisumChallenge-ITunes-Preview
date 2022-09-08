package com.erapps.itunespreview.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class SuggestionDaoTest {

    private lateinit var dataBase: ITunesPreviewDataBase
    private lateinit var dao: SuggestionsDao

    @Before
    fun setup() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ITunesPreviewDataBase::class.java
        ).allowMainThreadQueries().build()

        dao = dataBase.suggestionsDao()
    }

    @After
    fun teardown() {
        dataBase.close()
    }

    @Test
    fun testInsertSuggestion() = runTest {
        val suggestionModel = SuggestionModel(currentTime.toInt(), "Dualipa")
        dao.insertSuggestion(suggestionModel)

        val suggestions = dao.getSuggestions()

        assertThat(suggestions).contains(suggestionModel)
    }

    @Test
    fun testClearSuggestions() = runTest {
        for (i in 1..10) {
            val suggestionModel = SuggestionModel(currentTime.toInt() + i, "Prince $i")
            dao.insertSuggestion(suggestionModel)
        }
        dao.clearSuggestions()

        val suggestions = dao.getSuggestions()

        assertThat(suggestions).hasSize(7)
    }

    @Test
    fun testRemoveDuplicates() = runTest {
        for (i in 1..10) {
            dao.insertSuggestion(SuggestionModel(0, "AC/DC"))
            dao.insertSuggestion(SuggestionModel(1, "Queen"))
        }

        dao.deleteDuplicates()

        val suggestions = dao.getSuggestions()

        assertThat(suggestions).hasSize(2)
    }
}