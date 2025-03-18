package com.oxiion.campusmen.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oxiion.campusmen.R
import com.oxiion.campusmen.viewmodels.CampusMenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarningScreen(
    navigateBack: () -> Unit,
    viewModel: CampusMenViewModel
) {
    val context = LocalContext.current
    val campusMan by viewModel.campusMan.collectAsState()

    // Calculate total earnings from earningList
    val totalEarnings = campusMan!!.earningList.sumOf { it.amount }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White,
                onClick = {
                    Toast.makeText(context, "Refreshing earnings...", Toast.LENGTH_SHORT).show()
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_currency_rupee_24),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp).padding(4.dp),
                            tint = Color.White
                        )
                        Text(
                            text = "Earnings",
                            style = MaterialTheme.typography.headlineMedium,
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
                .fillMaxSize().background(Color(0xFFE3F2FD))
                .padding(innerPadding)
        ) {
            // Earnings Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)) // Light Green
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_account_balance_wallet_24),
                            contentDescription = "Wallet Icon",
                            tint = Color(0xFF2E7D32), // Dark Green
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Total Earnings",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF388E3C) // Medium Green
                        )
                    }
                    Text(
                        text = "₹${String.format("%.2f", totalEarnings)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20) // Darkest Green
                    )
                }
            }

            // Earnings List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(campusMan!!.earningList) { earning ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)) // Light Green Tint
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left Side: Order Details
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = earning.orderId,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32) // Dark Green
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_access_time_24),
                                        contentDescription = "Order Time",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = earning.timestamp,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Right Side: Earnings Amount
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "₹${String.format("%.2f", earning.amount)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20) // Deep Green
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFF4CAF50), // Green Badge
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Earned",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

