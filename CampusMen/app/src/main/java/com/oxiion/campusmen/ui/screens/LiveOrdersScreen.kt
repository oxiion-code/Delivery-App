package com.oxiion.campusmen.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter
import com.oxiion.campusmen.R
import com.oxiion.campusmen.data.SharedPreferencesManager
import com.oxiion.campusmen.ui.components.OrderCard
import com.oxiion.campusmen.viewmodels.CampusMenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveOrdersScreen(
    viewModel: CampusMenViewModel,
    navigateToOrderDetailsScreen:(orderId:String)->Unit,
    navigateToPastOrderScreen: () -> Unit,
    navigateToEarningScreen: () -> Unit,
    navigateToSignInScreen: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val campusMan by viewModel.campusMan.collectAsState()
    val email=SharedPreferencesManager.getEmail(context)!!
    val code = SharedPreferencesManager.getVerificationCode(context)!!
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideNavigationBar(
                viewModel = viewModel,
                navigateToPastOrderScreen = navigateToPastOrderScreen,
                navigateToEarningScreen = navigateToEarningScreen,
                navigateToSignInScreen = navigateToSignInScreen,
                closeDrawer = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = Color(0xFF2196F3),
                    contentColor = Color.White,
                    onClick = {
                        viewModel.signin(email = email, code = code)
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
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(35.dp)
                                    .padding(4.dp)
                            )
                            Text(
                                text = "Live Orders",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(35.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        drawerState.open()  // Open Drawer when Menu Icon is clicked
                                    }
                                }
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
                    .background(Color(0xFFE3F2FD))
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    campusMan?.let { it ->
                        items(it.orders.filter { it.status.onProgress }.reversed()) {
                            OrderCard(
                                order = it,
                                onOrderCardClick = {
                                    navigateToOrderDetailsScreen(it.id)
                                }
                            )
                        }
                    }
                }
            } //
        }
    }
}

@Composable
fun SideNavigationBar(
    viewModel: CampusMenViewModel,
    navigateToPastOrderScreen: () -> Unit,
    navigateToEarningScreen: () -> Unit,
    navigateToSignInScreen: () -> Unit,
    closeDrawer:()->Unit
) {
    val context= LocalContext.current
    val campusman by viewModel.campusMan.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp)
            .background(Color(0xFF2196F3))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = rememberAsyncImagePainter(campusman?.imageUrl), // Replace with your image resource
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape),
            contentScale = ContentScale.Crop
        )

        // User Name
        campusman?.address?.let {
            Text(
                text = it.fullName,  // Replace with actual user name
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
        }
        // Menu Items
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateToPastOrderScreen()
                closeDrawer()
            }) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = Color.White
            )
            Text(
                text = "Past Orders",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateToEarningScreen()
                closeDrawer()
            }) {
            Icon(
                painter = painterResource(R.drawable.baseline_currency_rupee_24),
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = Color.White
            )
            Text(
                text = "Earning History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                SharedPreferencesManager.setLoggedIn(context =context, isLoggedIn = false )
                SharedPreferencesManager.saveEmail(context,"")
                SharedPreferencesManager.saveVerificationCode(context,"")
                navigateToSignInScreen()
                closeDrawer() }) {
            Icon(
                Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = Color.White
            )
            Text(
                text = "Logout",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )
        }
    }
}
