package com.example.cafeandino.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafeandino.ui.theme.CafeAndinoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    customerName: String,
    onBackToMenu: () -> Unit
) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Pedido confirmado") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Gracias, $customerName!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Tu pedido está en preparación.")

            Button(onClick = onBackToMenu) {
                Text("Volver al menú")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmationScreenPreview() {
    CafeAndinoTheme {
        ConfirmationScreen(customerName = "Camila", onBackToMenu = {})
    }
}
