package com.erapps.itunespreview.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.erapps.itunespreview.ui.utils.Constants.ALBUM_DETAILS_BASE_ROUTE
import com.erapps.itunespreview.ui.utils.Constants.LANDING_PAGE_BASE_ROUTE
import com.erapps.itunespreview.ui.utils.Constants.SEARCH_BASE_ROUTE
import com.erapps.itunespreview.ui.utils.Constants.SPLASH_BASE_ROUTE

sealed class NavigationItem(
    val baseRoute: String,
    private val navArgs: List<NavArgs> = emptyList()
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(baseRoute).plus(argKeys).joinToString("/")
    }

    val args = navArgs.map { navArgument(name = it.key) { type = it.navType } }

    //navigation objects
    //main navigation
    object Splash: NavigationItem(SPLASH_BASE_ROUTE)
    object LandingPage: NavigationItem(LANDING_PAGE_BASE_ROUTE)
    // search navigation
    object Search: NavigationItem(SEARCH_BASE_ROUTE)
    object AlbumDetails: NavigationItem(ALBUM_DETAILS_BASE_ROUTE)
}

enum class NavArgs(val key: String, val navType: NavType<*>) {}
