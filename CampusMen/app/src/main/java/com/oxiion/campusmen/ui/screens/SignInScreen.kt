package com.oxiion.campusmen.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oxiion.campusmen.data.DataState
import com.oxiion.campusmen.data.SharedPreferencesManager
import com.oxiion.campusmen.viewmodels.CampusMenViewModel

@Composable
fun SignInScreen(
    paddingValues: PaddingValues,
    viewModel: CampusMenViewModel,
    navigateToLiveOrdersScreen:()->Unit
){
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    val context= LocalContext.current
    val isLoading = remember { mutableStateOf(false)}
    val signInState by viewModel.signinState.collectAsState()
    Column (
        modifier = Modifier.fillMaxSize().background(Color(0xFFE3F2FD)).padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(text="Sign In",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2196F3))
        OutlinedTextField(
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text(text = "Email") },
            modifier = Modifier.padding(vertical = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color(0xFF9E9E9E)
            )
        )
        OutlinedTextField(
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            value = code,
            onValueChange = {
                code = it
            },
            label = { Text(text = "Code") },
            modifier = Modifier.padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color(0xFF9E9E9E)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.signin(email,code)
        }, enabled = code.isNotEmpty() && email.isNotEmpty(),
            modifier = Modifier.size(180.dp,70.dp).padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                disabledContainerColor = Color(0xFF9E9E9E)
            )) {
            Text("Confirm Signin")
        }
    }
    if(isLoading.value){
        LoadingDialog(isLoading)
    }
    when(signInState){
        is DataState.Error -> {
            isLoading.value=false
            Toast.makeText(context, (signInState as DataState.Error).message , Toast.LENGTH_SHORT).show()
            viewModel.resetSignInState()
        }
        DataState.Idle ->
        {}
        DataState.Loading -> {
            isLoading.value=true
        }
        DataState.Success -> {
            isLoading.value=false
            navigateToLiveOrdersScreen()
            viewModel.resetSignInState()
        }
    }
}