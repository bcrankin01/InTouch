package com.example.current

data class User(val username: String, var connections: List<Connection> = emptyList())
