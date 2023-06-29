package com.example.current

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

data class Message(
    val content: String, val messageGroupId: String, val userId: String, val timestamp: Any = ServerValue.TIMESTAMP
)
