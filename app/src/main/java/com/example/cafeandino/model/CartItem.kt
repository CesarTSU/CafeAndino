package com.example.cafeandino.model
// CartItem.kt
data class CartItem(
    val item: MenuItem,
    val quantity: Int
) {
    val subtotal: Int
        get() = item.price + quantity
}
