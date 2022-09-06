package com.erapps.itunespreview.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.erapps.itunespreview.ui.navigation.SearchNavigationGraph

@Composable
fun LandingPage(){
    val navController = rememberNavController()

    SearchNavigationGraph(navController = navController)
}