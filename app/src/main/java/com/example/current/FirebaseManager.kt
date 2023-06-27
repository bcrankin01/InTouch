package com.example.current

import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseManager {

    private var database = Firebase.database.reference
    private var currentUser = Firebase.auth.currentUser

    fun signIn(email: String, password: String, navController: NavController) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign-in success, do something
                    navController.navigate(Screen.HomeScreen.route)
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        // Handle invalid user error
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        // Handle invalid credentials error
                    } catch (e: Exception) {
                        // Handle other exceptions
                    }
                }
            }
    }

    fun createUser(userId: String, name: String) {
        // Use the user ID as the key for the user data in the Realtime Database
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        // Create a user object or map with the relevant user data
        val userData = User(username = name)

        // Set the user data in the Realtime Database using the user ID as the key
        userRef.setValue(userData)
    }

    fun sendMessage(content: String) {

    }
}