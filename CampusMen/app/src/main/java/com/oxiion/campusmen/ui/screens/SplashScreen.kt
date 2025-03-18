package com.oxiion.campusmen.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.oxiion.campusmen.data.DataState
import com.oxiion.campusmen.data.SharedPreferencesManager
import com.oxiion.campusmen.ui.screens.LoadingDialog
import com.oxiion.campusmen.viewmodels.CampusMenViewModel


@Composable
fun SplashScreenContent(
    navigateToSignInScreen: () -> Unit,
    navigateToLiveScreen: () -> Unit,
    viewModel: CampusMenViewModel
) {
    val context = LocalContext.current
    val signInState by viewModel.signinState.collectAsState()
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (SharedPreferencesManager.isLoggedIn(context)) {
            val savedEmail = SharedPreferencesManager.getEmail(context)
            val savedCode = SharedPreferencesManager.getVerificationCode(context)
            if (!savedEmail.isNullOrEmpty() && !savedCode.isNullOrEmpty()) {
                viewModel.signin(savedEmail, savedCode)
            } else {
                navigateToSignInScreen()
            }
        } else {
            navigateToSignInScreen()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF2196F3)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading.value) {
            LoadingDialog(isLoading)
        }
    }

    when (signInState) {
        is DataState.Error -> {
            isLoading.value = false
            Toast.makeText(context, (signInState as DataState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.resetSignInState()
        }
        DataState.Idle -> Unit
        DataState.Loading -> {
            isLoading.value = true
        }
        DataState.Success -> {
            isLoading.value = false
            navigateToLiveScreen()
            viewModel.resetSignInState()
        }
    }
}
