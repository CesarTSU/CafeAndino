
package com.example.cafeandino.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafeandino.R
import com.example.cafeandino.model.CartItem
import com.example.cafeandino.model.MenuItem
import com.example.cafeandino.viewmodel.CartViewModel

// Esta función conoce al ViewModel
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onCheckoutClick: () -> Unit,
    onBack: () -> Unit
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalAmount by cartViewModel.totalAmount.collectAsState()

    CartScreenContent(
        cartItems = cartItems,
        totalAmount = totalAmount,
        onIncrease = { itemId -> cartViewModel.increaseQuantity(itemId) },
        onDecrease = { itemId -> cartViewModel.decreaseQuantity(itemId) },
        onRemove = { itemId -> cartViewModel.removeItem(itemId) },
        onClearCart = { cartViewModel.clearCart() },
        onCheckoutClick = onCheckoutClick,
        onBack = onBack
    )
}

// Esta función NO conoce al ViewModel: solo dibuja lo que recibe
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenContent(
    cartItems: List<CartItem>,
    totalAmount: Int,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    onClearCart: () -> Unit,
    onCheckoutClick: () -> Unit,
    onBack: () -> Unit
) {
    // Estado propio de esta pantalla: ¿está abierto el diálogo de confirmación?
    var showClearDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tu carrito") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Volver") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (cartItems.isEmpty()) {
                Text("Tu carrito está vacío")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems, key = { it.item.id }) { cartItem ->
                        CartItemRow(
                            cartItem = cartItem,
                            onIncrease = { onIncrease(cartItem.item.id) },
                            onDecrease = { onDecrease(cartItem.item.id) },
                            onRemove = { onRemove(cartItem.item.id) }
                        )
                    }
                }

                Text(
                    text = "Total: $${totalAmount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = onCheckoutClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ir a confirmar pedido")
                }

                OutlinedButton(
                    onClick = { showClearDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Vaciar carrito")
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Vaciar carrito") },
            text = { Text("Se quitarán todos los productos. ¿Continuar?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearCart()
                        showClearDialog = false
                    }
                ) {
                    Text("Vaciar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = cartItem.item.name, fontWeight = FontWeight.Bold)
        Text(
            text = "$${cartItem.item.price} c/u",
            style = MaterialTheme.typography.bodySmall
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onDecrease) { Text("-") }
            Text(text = "${cartItem.quantity}")
            OutlinedButton(onClick = onIncrease) { Text("+") }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onRemove) { Text("Quitar") }
        }

        Text(
            text = "Subtotal: $${cartItem.subtotal}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    val americano = MenuItem(1, "Café Americano", "Café negro suave, 250ml", 1800, R.drawable.logo)
    val cappuccino = MenuItem(2, "Cappuccino", "Espresso con leche vaporizada", 2200, R.drawable.logo)

    CartScreenContent(
        cartItems = listOf(CartItem(americano, 2), CartItem(cappuccino, 1)),
        totalAmount = 5800,
        onIncrease = {},
        onDecrease = {},
        onRemove = {},
        onClearCart = {},
        onCheckoutClick = {},
        onBack = {}
    )
}