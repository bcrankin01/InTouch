package com.example.current

data class Connection(val members: List<String> = emptyList(), val status: String = "closed", val streak: Int = 0)
