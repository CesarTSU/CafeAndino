package com.example.cafeandino.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

// CartItem.kt
data class CartItem(
    val item: MenuItem,
    val quantity: Int
) {
    val subtotal: Int
        //Error sencillo habia una suma donde debia tener una multiplicacion
        get() = item.price * quantity
}

