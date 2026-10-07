package com.example.cafeandino.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _orderCount = MutableStateFlow(0)
    val orderCount: StateFlow<Int> = _orderCount.asStateFlow()

    private val _lastCustomerName = MutableStateFlow<String?>(null)
    val lastCustomerName: StateFlow<String?> = _lastCustomerName.asStateFlow()

    fun addOrder() {
        _orderCount.value += 1
    }

    fun confirmOrder(customerName: String) {
        _lastCustomerName.value = customerName
        _orderCount.value = 0
    }
}
