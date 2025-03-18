package com.oxiion.campusmen.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oxiion.campusmen.ui.components.OrderCard
import com.oxiion.campusmen.viewmodels.CampusMenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastOrders(
     viewModel: CampusMenViewModel,
     navigateBack:()->Unit
 ) {
    val campusMan by viewModel.campusMan.collectAsState()
    val context = LocalContext.current
    Scaffold (
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
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(35.dp).padding(4.dp)
                        )
                        Text(
                            text = "Past Orders",
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
                        modifier = Modifier.size(35.dp).clickable {
                            navigateBack()
                        }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White
                )
            )
        }
    ){ innerPadding->
        Column (
            modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFFE3F2FD))
        ){
            LazyColumn(modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                campusMan?.let { it ->
                    items(it.orders.filter { it.status.delivered }.reversed()){
                        OrderCard(
                            order = it,
                            onOrderCardClick = {
                            }
                        )
                    }
                }
            }
        }
    }

}