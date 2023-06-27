package com.example.current

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object MessagesScreen: Screen("messages_screen")
    object SignupScreen: Screen("Signup_screen")
    object SigninScreen: Screen("Signin_screen")
}

