package com.erapps.itunespreview.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erapps.itunespreview.ui.screens.LandingPage
import com.erapps.itunespreview.ui.screens.SplashScreen

@Composable
fun MainNavigationGraph() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationItem.Splash.baseRoute) {
        composable(route = NavigationItem.Splash.baseRoute){
            SplashScreen {
                navController.popBackStack()
                navController.navigate(NavigationItem.LandingPage.baseRoute)
            }
        }
        composable(NavigationItem.LandingPage.baseRoute){
            LandingPage()
        }
    }
}