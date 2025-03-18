package com.oxiion.campusmen.data

sealed class DataState{
    data object Idle : DataState()
    data object Loading : DataState()
    data object Success : DataState()
    data class Error(val message: String) : DataState()
}