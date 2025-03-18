package com.oxiion.campusmen.domain.usecases

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.oxiion.campusmen.data.SharedPreferencesManager
import com.oxiion.campusmen.domain.repositories.CampusMenRepository
import com.oxiion.campusmen.model.Address
import com.oxiion.campusmen.model.CampusMan
import com.oxiion.campusmen.model.Earning
import com.oxiion.campusmen.model.Order
import com.oxiion.campusmen.model.OrderStatus
import com.oxiion.campusmen.model.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CampusMenRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : CampusMenRepository {
    override suspend fun signin(username: String, code: String): Result<CampusMan> {
        return try {
            val adminQuerySnapshot = firestore.collection("admins").get().await()

            for (adminDoc in adminQuerySnapshot.documents) {
                val employeeList = adminDoc.get("employeeList") as? List<Map<String, Any>> ?: emptyList()

                for (employee in employeeList) {
                    val email = (employee["address"] as? Map<String, Any>)?.get("email") as? String ?: continue
                    val id = employee["id"] as? String ?: continue

                    if (email == username && id == code) {
                        // Extracting address details
                        val addressMap = employee["address"] as? Map<String, Any> ?: emptyMap()
                        val address = Address(
                            fullName = addressMap["fullName"] as? String ?: "",
                            phoneNumber = addressMap["phoneNumber"] as? String ?: "",
                            hostelNumber = addressMap["hostelNumber"] as? String ?: "",
                            roomNumber = addressMap["roomNumber"] as? String ?: "",
                            rollNumber = addressMap["rollNumber"] as? String ?: "",
                            email = addressMap["email"] as? String ?: ""
                        )

                        val campusMan = CampusMan(
                            id = id,
                            imageUrl = employee["imageUrl"] as? String ?: "",
                            college = adminDoc.getString("collagename") ?: "",
                            address = address, // Include the extracted address
                            memberStockItems = (employee["memberStockItems"] as? List<Map<String, Any>>)?.map { item ->
                                Product(
                                    id = item["id"] as? String ?: "",
                                    name = item["name"] as? String ?: "",
                                    category = item["category"] as? String ?: "",
                                    quantity = (item["quantity"] as? Long)?.toInt() ?: 0,
                                    rating = (item["rating"] as? Double) ?: 0.0,
                                    available = item["available"] as? Boolean ?: true,
                                    discount = item["discount"] as? Double,
                                    description = item["description"] as? String ?: "",
                                    price = (item["price"] as? Double) ?: 0.0,
                                    image = item["image"] as? String ?: ""
                                )
                            } ?: emptyList(),
                            orders = (employee["orders"] as? List<Map<String, Any>>)?.map { orderData ->
                                val statusMap = orderData["status"] as? Map<String, Any> ?: emptyMap()

                                Order(
                                    id = orderData["id"] as? String ?: "",
                                    confirmationCode = orderData["confirmationCode"] as? String ?: "",
                                    timestamp = (orderData["timestamp"] as? Long) ?: 0L,
                                    items = (orderData["items"] as? List<Map<String, Any>>)?.map { item ->
                                        Product(
                                            id = item["id"] as? String ?: "",
                                            name = item["name"] as? String ?: "",
                                            category = item["category"] as? String ?: "",
                                            quantity = (item["quantity"] as? Long)?.toInt() ?: 0,
                                            rating = (item["rating"] as? Double) ?: 0.0,
                                            available = item["available"] as? Boolean ?: true,
                                            discount = item["discount"] as? Double,
                                            description = item["description"] as? String ?: "",
                                            price = (item["price"] as? Double) ?: 0.0,
                                            image = item["image"] as? String ?: ""
                                        )
                                    } ?: emptyList(),
                                    quantity = (orderData["quantity"] as? Long)?.toInt() ?: 0,
                                    totalPrice = (orderData["totalPrice"] as? Number)?.toDouble() ?: 0.0,
                                    status = OrderStatus(
                                        delivered = statusMap["delivered"] as? Boolean ?: false,
                                        cancelled = statusMap["cancelled"] as? Boolean ?: false,
                                        onProgress = statusMap["onProgress"] as? Boolean ?: false
                                    ),
                                    receipt = orderData["receipt"] as? String ?: ""
                                )
                            } ?: emptyList(),
                            pastOrders = emptyList(),
                            earningList = (employee["myEarning"] as? List<Map<String, Any>>)?.map {
                                Earning(
                                    amount = (it["amount"] as?  Number)?.toDouble() ?: 0.0,
                                    orderId = (it["orderId"] as? String)?:"",
                                    timestamp = it["timestamp"] as? String ?: ""
                                ) //val totalPrice = (orderData["totalPrice"] as? Number)?.toDouble() ?: 0.0
                            } ?: emptyList()
                        )
                        return Result.success(campusMan)
                    }
                }
            }
            Result.failure(Exception("No matching employee found"))
        } catch (e: Exception) {
            Log.e("signin firebase", e.localizedMessage.toString())
            return Result.failure(e)
        }
    }

    override suspend fun confirmOrder(orderId: String, email: String, code: String): Result<Boolean> {
        return try {
            val adminQuerySnapshot = firestore.collection("admins").get().await()

            for (adminDoc in adminQuerySnapshot.documents) {
                val employeeList = adminDoc.get("employeeList") as? List<Map<String, Any>> ?: emptyList()

                for (employee in employeeList) {
                    val employeeEmail = (employee["address"] as? Map<String, Any>)?.get("email") as? String ?: continue
                    val employeeId = employee["id"] as? String ?: continue

                    if (employeeEmail == email) {
                        val ordersList = (employee["orders"] as? List<Map<String, Any>>)?.toMutableList() ?: mutableListOf()

                        // Find the matching order
                        val orderIndex = ordersList.indexOfFirst { it["id"] == orderId }
                        if (orderIndex != -1) {
                            val orderData = ordersList[orderIndex] as MutableMap<String, Any>
                            val totalPrice = (orderData["totalPrice"] as? Double) ?: 0.0

                            // Update order status
                            val statusMap = orderData["status"] as? MutableMap<String, Any> ?: mutableMapOf()
                            statusMap["delivered"] = true
                            statusMap["onProgress"] = false
                            orderData["status"] = statusMap

                            // Add earnings entry (10% of totalPrice)
                            val earningsList = (employee["myEarning"] as? MutableList<Map<String, Any>>)?.toMutableList() ?: mutableListOf()
                            val newEarning = mapOf(
                                "amount" to (totalPrice * 0.10),
                                "orderId" to orderId,
                                "timestamp" to formatTimestamp(System.currentTimeMillis().toString())
                            )
                            earningsList.add(newEarning)

                            // Update Firestore with modified data
                            val updatedEmployeeData = employee.toMutableMap().apply {
                                put("orders", ordersList)
                                put("myEarning", earningsList)
                            }

                            val updatedEmployeeList = employeeList.toMutableList()
                            updatedEmployeeList[employeeList.indexOf(employee)] = updatedEmployeeData

                            adminDoc.reference.update("employeeList", updatedEmployeeList).await()

                            // Delete OTP document
                            firestore.collection("Otp").document(code).delete().await()

                            // Update User Orders Collection
                            val usersSnapshot = firestore.collection("Users").get().await()
                            for (userDoc in usersSnapshot.documents) {
                                val userOrdersRef = userDoc.reference.collection("orders").document(orderId)
                                val orderSnapshot = userOrdersRef.get().await()
                                if (orderSnapshot.exists()) {
                                    userOrdersRef.update(
                                        mapOf(
                                            "status.delivered" to true,
                                            "status.onProgress" to false
                                        )
                                    ).await()
                                }
                            }

                            return Result.success(true)
                        }
                    }
                }
            }
            Result.failure(Exception("No matching order found"))
        } catch (e: Exception) {
            Log.e("confirmOrder", e.localizedMessage.toString())
            Result.failure(e)
        }
    }

}
fun formatTimestamp(timestampStr: String): String {
    return try {
        val timestamp = timestampStr.toLong() // Convert String to Long
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        format.format(date)
    } catch (e: Exception) {
        "Invalid timestamp"
    }
}