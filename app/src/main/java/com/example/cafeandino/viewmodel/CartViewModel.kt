package com.example.cafeandino.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafeandino.model.CartItem
import com.example.cafeandino.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow(mutableListOf<CartItem>())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Datos derivados: se recalculan solos cada vez que cambia el carrito
    val orderCount: StateFlow<Int> = _cartItems
        .map { items -> items.sumOf { it.quantity } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val totalAmount: StateFlow<Int> = _cartItems
        .map { items -> items.sumOf { it.subtotal } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    private val _lastCustomerName = MutableStateFlow<String?>(null)
    val lastCustomerName: StateFlow<String?> = _lastCustomerName.asStateFlow()

    fun addItem(menuItem: MenuItem) {
        val existing = _cartItems.value.firstOrNull { it.item.id == menuItem.id }
        // Segundo error cuando se agregaba algo al carrito no se sumaba como 1 si no pasaba a 2 directo arreglado
        if (existing == null) {
            _cartItems.value = (_cartItems.value + CartItem(menuItem, 1)).toMutableList()
        }
        else {
            increaseQuantity(menuItem.id)
        }
    }

    fun increaseQuantity(itemId: Int) {
        _cartItems.value = _cartItems.value
            .map { if (it.item.id == itemId) it.copy(quantity = it.quantity + 1) else it }
            .toMutableList()
    }

    // Se arregla error de la funcion de descrecimiento que cuando uno le daba al - en el carrito este restaba y pasaba a negativo
    fun decreaseQuantity(itemId: Int) {
        val item = _cartItems.value.firstOrNull { it.item.id == itemId } ?: return
        if (item.quantity <= 1) {
            removeItem(itemId)
        } else {
            _cartItems.value = _cartItems.value
                .map { if (it.item.id == itemId) it.copy(quantity = it.quantity - 1) else it }
                .toMutableList()
        }
    }


    fun removeItem(itemId: Int) {
        _cartItems.value = _cartItems.value.filter { it.item.id != itemId }.toMutableList()
    }

    fun clearCart() {
        _cartItems.value = mutableListOf()
    }

    /** Se llama cuando el formulario se confirmó correctamente. */

    //Faltaba que cuando se confirmara la funcion de orden se limpiara el carrito con el clearcart
    fun confirmOrder(customerName: String) {
        _lastCustomerName.value = customerName
        clearCart()
    }

}
