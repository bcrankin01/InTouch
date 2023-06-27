package com.example.current

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


private var firebaseManager = FirebaseManager()

@Composable
fun SigninScreen(
    navController: NavController
) {

    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign In",
            color = Color.White
        )

        OutlinedTextField(
            modifier = Modifier
                .background(Color.White),
            value = username,
            onValueChange = { text ->
                username = text
            },
            label = { Text("Username") },
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )

        OutlinedTextField(
            modifier = Modifier
                .background(Color.White),
            value = password,
            onValueChange = { text ->
                password = text
            },
            label = { Text("Password") }, // Hint for the password field
        )

        Button(
            onClick = {
                Log.d("OnSignIn", "Email: ${username}, Password: $password")
                firebaseManager.signIn(username, password, navController)
            }
        ) {
            Text(text = "Sign in")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account?",
                color = Color.White
            )
            TextButton(onClick = {
                navController.navigate(route = Screen.SignupScreen.route)
            }) {
                Text(
                    text = "\tRegister",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun signup(phoneNumber: String) {

}


@Composable
@Preview
fun SigninScreenPreview() {
    SigninScreen(
        navController = rememberNavController()
    )
}