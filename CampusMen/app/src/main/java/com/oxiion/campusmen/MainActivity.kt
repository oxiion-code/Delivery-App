package com.oxiion.campusmen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.oxiion.campusmen.navigation.StartNavigation
import com.oxiion.campusmen.ui.screens.LiveOrdersScreen
import com.oxiion.campusmen.ui.screens.SignInScreen
import com.oxiion.campusmen.ui.theme.CampusMenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusMenTheme {
                val navController= rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   StartNavigation(navController = navController, paddingValues = innerPadding)
                }
            }
        }
    }
}



