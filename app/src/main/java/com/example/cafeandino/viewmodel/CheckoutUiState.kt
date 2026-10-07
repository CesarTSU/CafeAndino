package com.example.cafeandino.viewmodel

import com.example.cafeandino.model.PaymentMethod

data class CheckoutUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val tipPercent: Int = 10,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val wantsInvoice: Boolean = false,
    val acceptsTerms: Boolean = false,
    val showErrors: Boolean = false
) {

    val nameError: String?
        get() = if (name.trim().length < 3) "Ingresa tu nombre completo" else null

    val emailError: String?
        get() = if (!EMAIL_PATTERN.matches(email.trim())) "Correo no válido. Ejemplo: nombre@correo.cl" else null

    val phoneError: String?
        get() = if (phone.length != 9) "El teléfono debe tener 9 dígitos" else null

    val addressError: String?
        get() = if (address.trim().length < 5) "Ingresa una dirección de entrega" else null

    val termsError: String?
        get() = if (!acceptsTerms) "Debes aceptar las condiciones de entrega" else null

    val isValid: Boolean
        get() = nameError == null &&
                emailError == null &&
                phoneError == null &&
                addressError == null &&
                termsError == null

    companion object {
        private val EMAIL_PATTERN =
            Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
