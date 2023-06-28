package com.example.current

import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
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

    interface FetchUsersBySearchTextListener {
        fun onSuccess(users: List<Pair<String,String>>)
        fun onError(databaseError: DatabaseError)
    }
    fun fetchUsersBySearchText(searchText: String, listener: FetchUsersBySearchTextListener): List<Pair<String, String>> {

        val usersRef = database.child("users")

        val userList: MutableList<Pair<String, String>> = mutableListOf()

        val query: Query = usersRef.orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val userId = userSnapshot.key
                    val username = userSnapshot.child("username").value as String
                    val userPair = Pair(userId.toString(), username)
                    userList.add(userPair)
                }
                listener.onSuccess(userList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur
            }
        })

        return userList
    }

    fun createNewConnection(users: List<Pair<String, String>>) {

        //get list of ids
        var idList = users.map { it.first }
        idList += currentUser?.uid.toString()
        val connectionData = Connection(idList)

        //push connection to connections collection, grab the key, set value of ref
        val connectionRef = database.child("connections").push()
        val connectionId = connectionRef.key.toString()
        connectionRef.setValue(connectionData)

        //add key to each users connections list (myConnections)
        for (userId in idList) {
            val userConnectionsRef = database.child("users").child(userId).child("myConnections")
            userConnectionsRef.push().setValue(connectionId)
        }
    }

    fun sendMessage(content: String) {

    }
}