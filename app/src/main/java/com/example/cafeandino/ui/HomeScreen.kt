// HomeScreen.kt
package com.example.cafeandino.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafeandino.R
import com.example.cafeandino.model.MenuItem
import com.example.cafeandino.viewmodel.CartViewModel
import com.example.cafeandino.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onCheckoutClick: () -> Unit
) {
    val menuItems by homeViewModel.menuItems.collectAsState()
    val orderCount by cartViewModel.orderCount.collectAsState()
    val lastCustomerName by cartViewModel.lastCustomerName.collectAsState()

    HomeScreenContent(
        menuItems = menuItems,
        orderCount = orderCount,
        lastCustomerName = lastCustomerName,
        onAdd = { menuItem -> cartViewModel.addItem(menuItem) },
        onProductClick = onProductClick,
        onCartClick = onCartClick,
        onCheckoutClick = onCheckoutClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    menuItems: List<MenuItem>,
    orderCount: Int,
    lastCustomerName: String?,
    onAdd: (MenuItem) -> Unit,
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onCheckoutClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Café Andino") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Pedidos en el carrito: $orderCount",
                fontWeight = FontWeight.Bold
            )

            if (lastCustomerName != null) {
                Text(
                    text = "Último pedido confirmado a nombre de $lastCustomerName",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCartClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ver carrito")
                }
                Button(
                    onClick = onCheckoutClick,
                    enabled = orderCount > 0,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Confirmar pedido")
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(menuItems) { item ->
                    MenuItemCard(
                        item = item,
                        onAdd = { onAdd(item) },
                        onClick = { onProductClick(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItemCard(
    item: MenuItem,
    onAdd: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.name,
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, fontWeight = FontWeight.Bold)
            Text(text = item.description, style = MaterialTheme.typography.bodySmall)
            Text(text = "$${item.price}", style = MaterialTheme.typography.bodyMedium)
        }

        Button(onClick = onAdd) {
            Text("Agregar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        menuItems = listOf(
            MenuItem(1, "Café Americano", "Café negro suave, 250ml", 1800, R.drawable.logo),
            MenuItem(2, "Cappuccino", "Espresso con leche vaporizada", 2200, R.drawable.logo)
        ),
        orderCount = 2,
        lastCustomerName = null,
        onAdd = {},
        onProductClick = {},
        onCartClick = {},
        onCheckoutClick = {}
    )
}
