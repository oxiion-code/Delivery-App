package com.oxiion.campusmen.model

import com.google.firebase.Timestamp

data class Earning(
    val amount: Double = 0.0,
    val orderId:String="",
    val timestamp: String = ""
)
