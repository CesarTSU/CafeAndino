package com.example.cafeandino.model
// CartItem.kt
data class CartItem(
    val item: MenuItem,
    val quantity: Int
) {
    val subtotal: Int
        //Error sencillo habia una suma donde debia tener una multiplicacion
        get() = item.price * quantity
}
