package com.oxiion.campusmen.model


data class Admin(
    val name: String="Rudra Narayan Rath",
    val securityCode: String="",
    var email:String="",
    var role: String = "admin",
    var stockItems:List<Product> = listOf(),
    var collagename:String="",
    var employeeList:List<CampusMan> = listOf()
)
