package com.example.cafeandino.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cafeandino.model.MenuItem
import com.example.cafeandino.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val repository: MenuRepository = MenuRepository()
) : ViewModel() {

    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems.asStateFlow()

    init {
        _menuItems.value = repository.getMenu()
    }
}
