package com.erapps.itunespreview.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.erapps.itunespreview.ui.screens.details.AlbumDetailsScreen
import com.erapps.itunespreview.ui.screens.search.SearchScreen
import com.erapps.itunespreview.ui.shared.SharedViewModel

@Composable
fun SearchNavigationGraph(navController: NavHostController) {

    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(navController = navController, startDestination = NavigationItem.Search.baseRoute) {
        composable(NavigationItem.Search) {
            SearchScreen { album ->
                sharedViewModel.addAlbum(album!!)
                navController.navigate(NavigationItem.AlbumDetails.route)
            }
        }
        composable(NavigationItem.AlbumDetails) {
            AlbumDetailsScreen(
                sharedViewModel = sharedViewModel
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