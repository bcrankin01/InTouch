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
import com.google.firebase.auth.FirebaseAuth


private var firebaseManager = FirebaseManager()

@Composable
fun SignupScreen(
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
            .padding(16.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",
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
                signup(username, password, navController)
            }
        ) {
            Text(
                text = "Register",
                color = Color.White
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Already have an account?")
            TextButton(onClick = {
                navController.navigate(route = Screen.SigninScreen.route)
            }) {
                Text(
                    text = "\tSign in",
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

private fun signup(email: String, password: String, navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // User creation successful, a new user is created
                val user = auth.currentUser
                firebaseManager.createUser(user?.uid.toString(), email)
                navController.navigate(Screen.HomeScreen.route)
            } else {
                // User creation failed, handle the error
                val exception = task.exception
            }
        }
}


@Composable
@Preview
fun SignupScreenPreview() {
    SignupScreen(
        navController = rememberNavController()
    )
}