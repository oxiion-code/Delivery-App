package com.oxiion.campusmen.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.oxiion.campusmen.ui.screens.SplashScreenContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oxiion.campusmen.ui.screens.EarningScreen
import com.oxiion.campusmen.ui.screens.LiveOrdersScreen
import com.oxiion.campusmen.ui.screens.OrderDetailsScreen
import com.oxiion.campusmen.ui.screens.PastOrders
import com.oxiion.campusmen.ui.screens.SignInScreen
import com.oxiion.campusmen.viewmodels.CampusMenViewModel

@Composable
fun StartNavigation(
    navController: NavController,
    paddingValues: PaddingValues
) {
    val viewModel: CampusMenViewModel = hiltViewModel()
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screens.SplashScreen.route
    ) {
        composable(Screens.SplashScreen.route) {
            SplashScreenContent(
                navigateToSignInScreen = {
                    navController.navigate(Screens.SignIn.route) {
                        popUpTo(0) { inclusive = true } // Clears entire back stack
                        launchSingleTop = true
                    }
                },
                navigateToLiveScreen = {
                    navController.navigate(Screens.LiveOrders.route) {
                        popUpTo(0) { inclusive = true } // Clears entire back stack
                        launchSingleTop = true
                    }
                },
                viewModel = viewModel
            )
        }

        composable(Screens.SignIn.route) {
            SignInScreen(
                paddingValues = paddingValues,
                viewModel = viewModel,
                navigateToLiveOrdersScreen = {
                    navController.navigate(Screens.LiveOrders.route) {
                        popUpTo(0) { inclusive = true } // Clears entire back stack
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screens.LiveOrders.route) {
            LiveOrdersScreen(
                viewModel = viewModel,
                navigateToSignInScreen = {
                    navController.navigate(Screens.SignIn.route) {
                        popUpTo(0) { inclusive = true } // Clears all previous screens
                        launchSingleTop = true
                    }
                },
                navigateToEarningScreen = {
                    navController.navigate(Screens.Earning.route)
                },
                navigateToPastOrderScreen = {
                    navController.navigate(Screens.PastOrders.route)
                },
                navigateToOrderDetailsScreen = { orderId ->
                    navController.navigate("${Screens.OrderDetails.route}/$orderId") // Fixed navigation
                }
            )
        }

        composable("${Screens.OrderDetails.route}/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailsScreen(
                viewModel = viewModel,
                orderId = orderId,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screens.PastOrders.route) {
            PastOrders(viewModel = viewModel) {
                navController.navigateUp()
            }
        }

        composable(Screens.Earning.route) {
            EarningScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                viewModel = viewModel
            )
        }
    }
}
