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

    NavHost(navController = navController, startDestination = NavigationItem.Search.route) {
        composable(NavigationItem.Search) {
            SearchScreen { album ->
                navController.navigate(
                    NavigationItem.AlbumDetails.createRoute(
                        albumImageUrl = album!!.artworkUrl100,
                        albumName = album.collectionName,
                        artistName = album.artistName,
                        albumId = album.collectionId.toLong()
                    )
                )
            }
        }
        composable(NavigationItem.AlbumDetails) { entry ->

            val albumImageUrl = entry.arguments?.getString(NavArgs.AlbumImageURL.key)
            val albumName = entry.arguments?.getString(NavArgs.AlbumName.key)
            val artistName = entry.arguments?.getString(NavArgs.ArtistName.key)
            val albumId = entry.arguments?.getLong(NavArgs.AlbumId.key)
            requireNotNull(albumImageUrl)
            requireNotNull(albumName)
            requireNotNull(artistName)
            requireNotNull(albumId)

            AlbumDetailsScreen(
                AlbumDetailsItem(
                    imageURL = albumImageUrl,
                    albumName = albumName,
                    artistName = artistName,
                    albumId = albumId
                )
            ) {
                navController.popBackStack()
            }
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