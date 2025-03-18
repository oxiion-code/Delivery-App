package com.oxiion.campusmen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.oxiion.campusmen.domain.usecases.formatTimestamp
import com.oxiion.campusmen.model.Order

@Composable
fun OrderCard(
    order: Order,
    onOrderCardClick: (Order) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF90CAF9)
        ),
        onClick = {
            onOrderCardClick(order)
        }
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
                // Check if order.items is not empty before accessing the first item
                if (order.items.isNotEmpty()) {
                    val painter = rememberAsyncImagePainter(order.items[0].image)
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(70.dp, 80.dp)
                    )
                } else {
                    // Show a placeholder or a default image if the list is empty
                    Box(
                        modifier = Modifier
                            .size(70.dp, 80.dp)
                            .background(Color.Gray)
                    ) {
                        Text(
                            text = "No Image",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = order.id,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Text(
                    text = formatTimestamp(order.timestamp.toString()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                val (color, showText) = when {
                    order.status.delivered-> Pair(Color(0xFFC7F6C7), "Delivered")
                    order.status.cancelled -> Pair(Color(0xFFFFDAD6), "Cancelled")
                    order.status.onProgress -> Pair(Color.LightGray, "In Progress")
                    else -> Pair(Color(0xFFEAEAEA), "Unknown") // Default fallback case
                }
                Card(
                    modifier = Modifier.padding(top = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = color
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = showText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}