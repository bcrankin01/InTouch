package com.example.current

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

data class MessageGroup(val connectionId: String, val dateCreated: Any = ServerValue.TIMESTAMP)
