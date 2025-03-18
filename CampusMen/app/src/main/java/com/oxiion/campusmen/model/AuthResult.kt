package com.oxiion.campusmen.model

data class AuthResult(
    val uid: String="",
    val products:List<Product> = listOf()
)
