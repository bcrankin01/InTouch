package com.example.current

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

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
            route = "${Screen.MessagesScreen.route}/{messageGroupId}",
            arguments = listOf(navArgument("messageGroupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val messageGroupId = backStackEntry.arguments?.getString("messageGroupId")
            messageGroupId?.let { groupId ->
                MessagesScreen(navController, groupId)
            } ?: run {
                // Handle the case when messageGroupId is null or invalid
                // For example, navigate back or show an error message
                navController.popBackStack()
            }
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
