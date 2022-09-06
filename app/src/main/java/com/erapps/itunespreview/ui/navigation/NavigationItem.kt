package com.erapps.itunespreview.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

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
    object Splash: NavigationItem("splash")
    object LandingPage: NavigationItem("landing_page")
    // search navigation
    object Search: NavigationItem("search")
    object AlbumDetails: NavigationItem("album_details")
}

enum class NavArgs(val key: String, val navType: NavType<*>) {}
