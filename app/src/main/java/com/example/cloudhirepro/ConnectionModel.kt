package com.example.cloudhirepro

data class ConnectionModel(
    val id: String,
    val name: String,
    val detail: String,
    val qualification: String,
    val photoUrl: String,
    val messages: List<String> = mutableListOf()
)
