package com.oxiion.campusmen.domain.repositories

import com.oxiion.campusmen.model.CampusMan

interface CampusMenRepository {
    suspend fun signin(username: String, code: String):Result<CampusMan>
    suspend fun confirmOrder(orderId: String,email:String,code:String): Result<Boolean>
}