package com.example.cafeandino.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cafeandino.model.MenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: MenuItem?,
    orderCount: Int,
    onAdd: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle") },
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
            if (product == null) {
                Text("Producto no encontrado")
            } else {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier.size(120.dp)
                )
                Text(text = product.name, fontWeight = FontWeight.Bold)
                Text(text = product.description)
                Text(text = "Precio: $${product.price}")

                Button(onClick = onAdd) {
                    Text("Agregar al carrito")
                }
                Text(text = "Pedidos en el carrito: $orderCount")
            }
        }
    }
}
