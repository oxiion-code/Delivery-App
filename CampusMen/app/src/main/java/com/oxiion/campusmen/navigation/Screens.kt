package com.oxiion.campusmen.navigation

sealed class Screens(val route:String) {
    data object SplashScreen:Screens("splash_screen")
    data object SignIn : Screens("signin")
    data object LiveOrders:Screens("live_orders")
    data object OrderDetails : Screens("order_details")
    data object Earning:Screens("earning")
    data object PastOrders:Screens("past_orders")
}