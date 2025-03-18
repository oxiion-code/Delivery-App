package com.oxiion.campusmen.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.oxiion.campusmen.data.DataState
import com.oxiion.campusmen.data.SharedPreferencesManager
import com.oxiion.campusmen.domain.usecases.formatTimestamp
import com.oxiion.campusmen.model.Order
import com.oxiion.campusmen.viewmodels.CampusMenViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    viewModel: CampusMenViewModel,
    orderId: String,
    navigateBack: () -> Unit
) {
    val confirmationDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(false) }
    var code by remember { mutableStateOf("") }
    val email = SharedPreferencesManager.getEmail(context)!!
    val savedCode = SharedPreferencesManager.getVerificationCode(context)!!
    val confirmationState by viewModel.orderConfirmationState.collectAsState()
    val campusMan by viewModel.campusMan.collectAsState()
    val orders = campusMan?.orders!!
    val order = orders.find { it.id == orderId }!!

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White,
                onClick = {
                    Toast.makeText(context, "Refreshing orders...", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row {
                        Icon(
                            Icons.Filled.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                                .padding(4.dp)
                        )
                        Text(
                            text = orderId,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(35.dp)
                            .clickable { navigateBack() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFE3F2FD))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(order.items) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF29638A),
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(80.dp, 100.dp)
                                    .background(Color.White)
                            ) {
                                val painter = rememberAsyncImagePainter(order.image)
                                Image(
                                    painter = painter,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(70.dp, 80.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(text = order.name, style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Price: ${order.discount}", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Quantity: ${order.quantity}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFDAD6),
                            contentColor = Color.Black
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Total Price: ₹${order.totalPrice}", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Order ID: ${order.id}", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = formatTimestamp(order.timestamp.toString()), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 3.dp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(8.dp))

                    val earnings = (order.totalPrice * 0.10).roundToInt()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFA5D6A7), // Light Green Shade
                            contentColor = Color.Black
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Earnings from this Order",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Total Earnings: ₹$earnings",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }


                item {
                    Button(
                        onClick = { confirmationDialog.value = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E88E5),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(6.dp)
                    ) {
                        Text(text = "Confirm Order", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        if (confirmationDialog.value) {
            AlertDialog(
                containerColor = Color(0xFFE3F2FD),
                onDismissRequest = { confirmationDialog.value = false },
                title = { Text(text = "Confirm Order", color = Color(0xFF0D47A1)) },
                text = {
                    Column {
                        Text(text = "Please enter the verification code to confirm order.", color = Color(0xFF2196F3))
                        OutlinedTextField(
                            value = code,
                            onValueChange = { code = it },
                            label = { Text("Enter Code", color = Color(0xFF0D47A1)) }, // Dark blue label
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF0D47A1))
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0D47A1), // Dark blue border when focused
                                unfocusedBorderColor = Color(0xFF42A5F5), // Lighter blue border when unfocused // Cursor color dark blue
                                unfocusedLabelColor = Color(0xFF1565C0), // Label color when unfocused
                               focusedTextColor = Color(0xFF0D47A1), // Dark blue text color
                            )
                        )

                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (code.isNotEmpty() && code == order.confirmationCode) {
                                viewModel.confirmOrder(orderId = order.id, code = code, email = email)
                                confirmationDialog.value = false
                            } else {
                                Toast.makeText(context, "Please enter the correct code", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                    ) {
                        Text("Confirm", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { confirmationDialog.value = false }) {
                        Text("Cancel", color = Color(0xFF0D47A1))
                    }
                }
            )
        }

        if (isLoading.value) {
            LoadingDialog(isLoading)
        }

        when (confirmationState) {
            is DataState.Error -> {
                isLoading.value = false
                Toast.makeText(context, (confirmationState as DataState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetOrderConfirmationState()
            }

            DataState.Idle -> {}

            DataState.Loading -> {
                isLoading.value = true
            }

            DataState.Success -> {
                isLoading.value = false
                Toast.makeText(context, "Order Confirmed", Toast.LENGTH_SHORT).show()
                viewModel.resetOrderConfirmationState()
                navigateBack()
            }
        }
    }
}
