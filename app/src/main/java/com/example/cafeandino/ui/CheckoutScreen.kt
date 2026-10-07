package com.example.cafeandino.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafeandino.model.PaymentMethod
import com.example.cafeandino.ui.theme.CafeAndinoTheme
import com.example.cafeandino.viewmodel.CheckoutUiState
import com.example.cafeandino.viewmodel.CheckoutViewModel
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    itemCount: Int,
    onOrderConfirmed: (String) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    CheckoutForm(
        state = state,
        itemCount = itemCount,
        snackbarHostState = snackbarHostState,
        onNameChange = { viewModel.onNameChange(it) },
        onEmailChange = { viewModel.onEmailChange(it) },
        onPhoneChange = { viewModel.onPhoneChange(it) },
        onAddressChange = { viewModel.onAddressChange(it) },
        onTipChange = { viewModel.onTipChange(it) },
        onPaymentMethodChange = { viewModel.onPaymentMethodChange(it) },
        onInvoiceChange = { viewModel.onInvoiceChange(it) },
        onTermsChange = { viewModel.onTermsChange(it) },
        onSubmit = {
            if (viewModel.submit()) {
                onOrderConfirmed(state.name.trim())
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar("Revisa los campos marcados en rojo")
                }
            }
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutForm(
    state: CheckoutUiState,
    itemCount: Int,
    snackbarHostState: SnackbarHostState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onTipChange: (Int) -> Unit,
    onPaymentMethodChange: (PaymentMethod) -> Unit,
    onInvoiceChange: (Boolean) -> Unit,
    onTermsChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Confirmar pedido") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Volver") }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Productos en el carrito: $itemCount",
                fontWeight = FontWeight.Bold
            )

            // ---------- NOMBRE ----------
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = { Text("Nombre completo") },
                singleLine = true,
                isError = state.showErrors && state.nameError != null,
                supportingText = {
                    val error = state.nameError
                    if (state.showErrors && error != null) Text(error)
                    else Text("Como quieres que te llamemos")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // ---------- CORREO ----------
            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = state.showErrors && state.emailError != null,
                supportingText = {
                    val error = state.emailError
                    if (state.showErrors && error != null) Text(error)
                    else Text("Te enviaremos la confirmación")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            // ---------- TELÉFONO ----------
            OutlinedTextField(
                value = state.phone,
                onValueChange = onPhoneChange,
                label = { Text("Teléfono") },
                singleLine = true,
                isError = state.showErrors && state.phoneError != null,
                supportingText = {
                    val error = state.phoneError
                    if (state.showErrors && error != null) Text(error)
                    else Text("9 dígitos, sin el +56")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            // ---------- DIRECCIÓN ----------
            OutlinedTextField(
                value = state.address,
                onValueChange = onAddressChange,
                label = { Text("Dirección de entrega") },
                isError = state.showErrors && state.addressError != null,
                supportingText = {
                    val error = state.addressError
                    if (state.showErrors && error != null) Text(error)
                    else Text("Calle, número y comuna")
                },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider()

            // ---------- PROPINA (Slider) ----------
            Text(
                text = "Propina sugerida: ${state.tipPercent}%",
                fontWeight = FontWeight.Bold
            )
            Slider(
                value = state.tipPercent.toFloat(),
                onValueChange = { onTipChange(it.roundToInt()) },
                valueRange = 0f..20f,
                steps = 3
            )

            HorizontalDivider()

            // ---------- MÉTODO DE PAGO (RadioButton) ----------
            Text(text = "Método de pago", fontWeight = FontWeight.Bold)

            PaymentMethod.entries.forEach { method ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPaymentMethodChange(method) }
                ) {
                    RadioButton(
                        selected = state.paymentMethod == method,
                        onClick = { onPaymentMethodChange(method) }
                    )
                    Text(text = method.label)
                }
            }

            HorizontalDivider()

            // ---------- BOLETA (Switch) ----------
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(
                    checked = state.wantsInvoice,
                    onCheckedChange = onInvoiceChange
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Necesito boleta")
            }

            // ---------- CONDICIONES (Checkbox) ----------
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = state.acceptsTerms,
                    onCheckedChange = onTermsChange
                )
                Text(text = "Acepto las condiciones de entrega")
            }

            val termsError = state.termsError
            if (state.showErrors && termsError != null) {
                Text(
                    text = termsError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar pedido")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutFormPreview() {
    CafeAndinoTheme {
        CheckoutForm(
            state = CheckoutUiState(
                name = "Al",
                email = "correo-malo",
                phone = "123",
                showErrors = true
            ),
            itemCount = 3,
            snackbarHostState = remember { SnackbarHostState() },
            onNameChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onAddressChange = {},
            onTipChange = {},
            onPaymentMethodChange = {},
            onInvoiceChange = {},
            onTermsChange = {},
            onSubmit = {},
            onBack = {}
        )
    }
}
