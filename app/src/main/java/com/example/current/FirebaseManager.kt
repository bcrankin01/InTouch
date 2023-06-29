package com.example.current

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

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

    interface FetchConnectionsListener {
        fun onSuccess(connections: List<Pair<String, Connection>>)
        fun onError(databaseError: DatabaseError)
    }

    fun fetchMyConnections(listener: FetchConnectionsListener) {
        val userRef = database.child("users").child(currentUser?.uid.toString())

        userRef.child("myConnections").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val connectionIds = mutableListOf<String>()

                for (connectionSnapshot in dataSnapshot.children) {
                    val connectionId = connectionSnapshot.key as String
//                    Log.d("fetchConnections", connectionId)
                    connectionIds.add(connectionSnapshot.value.toString())
                }

                val connectionsRef = database.child("connections")

                connectionsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(connectionsSnapshot: DataSnapshot) {
                        val connectionsList = mutableListOf<Pair<String, Connection>>()

                        for (connectionSnapshot in connectionsSnapshot.children) {
                            val connection = connectionSnapshot.getValue(Connection::class.java)
//                            Log.d("fetchConnections", "${connection?.status}")

                            if (connection != null && connection.status != "closed") {
//                                Log.d("fetchConnections", "${connectionSnapshot.key.toString()}")

//                                connectionIds.forEach {
////                                    Log.d("fetchConnections", it)
//                                }
//                                Log.d("fetchConnections", "${connection?.status}")
                            }
                            if (connection != null && connectionIds.contains(connectionSnapshot.key.toString()) && connection.status != "closed") {
//                                Log.d("fetchConnections", "Accepted: ${connection?.status}")
//                                val members = mutableListOf<User>()
//                                val memberIds = connection.members
//
//                                for (memberId in memberIds) {
//                                    val memberSnapshot = dataSnapshot.child(memberId)
//                                    val user = memberSnapshot.getValue(User::class.java)
//                                    user?.let { members.add(it) }
//                                }

                                connectionsList.add(Pair(connectionSnapshot.key.toString(), connection))
                            }
                        }

                        listener.onSuccess(connectionsList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        listener.onError(databaseError)
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listener.onError(databaseError)
            }
        })
    }

    fun createMessageGroup(connectionId: String) {
        val messageGroup = MessageGroup(connectionId)
        val database = FirebaseDatabase.getInstance()
        val messageGroupRef = database.reference.child("messageGroups").push()
        messageGroupRef.setValue(messageGroup)
            .addOnSuccessListener {
                // Message group created successfully
            }
            .addOnFailureListener { exception ->
                // Handle the failure
            }
    }

    interface GetNewestMessageGroupByIdListener {
        fun onSuccess(messageGroupId: String)
        fun onError(errorMessage: String)
    }
    fun getNewestMessageGroupById(connectionId: String, listener: GetNewestMessageGroupByIdListener) {
        val database = FirebaseDatabase.getInstance()
        val messageGroupsRef = database.reference.child("messageGroups")
        val query = messageGroupsRef.orderByChild("connectionId").equalTo(connectionId).limitToLast(1)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var latestMessageGroupId: String? = null
                var latestTimestamp: Long = 0

                for (messageGroupSnapshot in dataSnapshot.children) {
                    val timestamp = messageGroupSnapshot.child("dateCreated").value as Long
                    if (timestamp > latestTimestamp) {
                        latestTimestamp = timestamp
                        latestMessageGroupId = messageGroupSnapshot.key
                    }
                }

                if (latestMessageGroupId != null) {
                    listener.onSuccess(latestMessageGroupId)
                } else {
                    listener.onError("No message groups found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listener.onError(databaseError.message)
            }
        })
    }

    interface FetchMessagesByGroupIdListener {
        fun onSuccess(messages: List<Message>)
        fun onError(errorMessage: String)
    }

    fun fetchMessagesByGroupId(messageGroupId: String, listener: FetchMessagesByGroupIdListener) {
        val database = FirebaseDatabase.getInstance()
        val messagesRef = database.reference.child("messages")
        val query = messagesRef.orderByChild("messageGroupId").equalTo(messageGroupId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messagesList = ArrayList<Message>()

                for (messageSnapshot in dataSnapshot.children) {
                    val content = messageSnapshot.child("content").value as String
                    val messageGroupId = messageSnapshot.child("messageGroupId").value as String
                    val userId = messageSnapshot.child("userId").value as String
                    val timestamp = messageSnapshot.child("timestamp").value as Long

                    val message = Message(content, messageGroupId, userId, timestamp)
                    messagesList.add(message)
                }

                // Sort messages by timestamp in ascending order
                messagesList.sortBy { message -> Date(message.timestamp as Long) }

                listener.onSuccess(messagesList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listener.onError(databaseError.message)
            }
        })
    }



    fun sendMessage(content: String, messageGroupId: String) {
        val msg = Message(content, messageGroupId, currentUser?.uid.toString())
        val messageRef = database.child("messages").push()
        messageRef.setValue(msg)
    }
}