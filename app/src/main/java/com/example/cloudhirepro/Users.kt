package com.example.cloudhirepro

data class Users (
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val mainAddress: String = "",
    var subAddress: String = "",
    var number: String = "",
    var description: String = "",
)
