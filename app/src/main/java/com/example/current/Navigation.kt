package com.example.current

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SigninScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.MessagesScreen.route
        ) {
            MessagesScreen(navController)
        }
        composable(
            route = Screen.SignupScreen.route
        ) {
            SignupScreen(navController)
        }
        composable(
            route = Screen.SigninScreen.route
        ) {
            SigninScreen(navController)
        }
    }
}
