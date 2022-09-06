package com.erapps.itunespreview.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.erapps.itunespreview.ui.screens.details.AlbumDetailsItem
import com.erapps.itunespreview.ui.screens.details.AlbumDetailsScreen
import com.erapps.itunespreview.ui.screens.search.SearchScreen

@Composable
fun SearchNavigationGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = NavigationItem.Search.route){
        composable(NavigationItem.Search) {
            SearchScreen(){

            }
        }
        composable(NavigationItem.AlbumDetails) {
            AlbumDetailsScreen(AlbumDetailsItem(
                imageURL = "",
                artistName = "",
                albumName = "",
                songsCount = 0,
                previewSoundUrl = ""
            ))
        }
    }
}

private fun NavGraphBuilder.composable(
    navItem: NavigationItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route = navItem.route, arguments = navItem.args) {
        content(it)
    }
}