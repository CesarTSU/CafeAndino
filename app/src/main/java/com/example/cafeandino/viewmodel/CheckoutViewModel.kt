package com.example.cafeandino.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cafeandino.model.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CheckoutViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value.trim())
    }

    fun onPhoneChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(9)
        _uiState.value = _uiState.value.copy(phone = digits)
    }

    fun onAddressChange(value: String) {
        _uiState.value = _uiState.value.copy(address = value)
    }

    fun onTipChange(value: Int) {
        _uiState.value = _uiState.value.copy(tipPercent = value)
    }

    fun onPaymentMethodChange(value: PaymentMethod) {
        _uiState.value = _uiState.value.copy(paymentMethod = value)
    }

    fun onInvoiceChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(wantsInvoice = value)
    }

    fun onTermsChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(acceptsTerms = value)
    }

    fun submit(): Boolean {
        _uiState.value = _uiState.value.copy(showErrors = true)
        return _uiState.value.isValid
    }

    fun reset() {
        _uiState.value = CheckoutUiState()
    }
}
