package com.oxiion.campusmen.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oxiion.campusmen.data.DataState
import com.oxiion.campusmen.data.SharedPreferencesManager
import com.oxiion.campusmen.domain.repositories.CampusMenRepository
import com.oxiion.campusmen.model.CampusMan
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampusMenViewModel @Inject constructor(
    private val repository: CampusMenRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _signinState = MutableStateFlow<DataState>(DataState.Idle)
    val signinState: StateFlow<DataState> = _signinState.asStateFlow()

    private val _campusMan = MutableStateFlow<CampusMan?>(null)
    val campusMan: StateFlow<CampusMan?> = _campusMan.asStateFlow()

    private val _orderConfirmationState = MutableStateFlow<DataState>(DataState.Idle)
    val orderConfirmationState: StateFlow<DataState> = _orderConfirmationState.asStateFlow()

    fun signin(email: String, code: String) {
        viewModelScope.launch {
            _signinState.value = DataState.Loading
            try {
                val result = repository.signin(email, code)
                if (result.isSuccess) {
                    _campusMan.value = result.getOrNull()
                    _signinState.value = DataState.Success
                    SharedPreferencesManager.saveEmail(context = context, email = email)
                    SharedPreferencesManager.saveVerificationCode(context = context, code = code)
                    SharedPreferencesManager.setLoggedIn(context = context, isLoggedIn = true)
                } else {
                    _signinState.value = DataState.Error("Sign-in failed")
                }
            } catch (e: Exception) {
                _signinState.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun confirmOrder(orderId: String, email: String, code: String) {
        viewModelScope.launch {
            _orderConfirmationState.value = DataState.Loading
            try {
                val result = repository.confirmOrder(orderId, email, code)
                if (result.isSuccess) {
                    _orderConfirmationState.value = DataState.Success
                } else {
                    _orderConfirmationState.value = DataState.Error("Order confirmation failed")
                }
            } catch (e: Exception) {
                _orderConfirmationState.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetSignInState() {
        _signinState.value = DataState.Idle
    }

    fun resetOrderConfirmationState() {
        _orderConfirmationState.value = DataState.Idle
    }
}
