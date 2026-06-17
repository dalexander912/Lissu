package com.lissu.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lissu.LissuApplication
import com.lissu.data.models.ShoppingList
import com.lissu.data.repositories.ShoppingListRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    val shoppingLists: StateFlow<List<ShoppingList>> = 
        shoppingListRepository.getShoppingListsWithItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun deleteList(list: ShoppingList) {
        viewModelScope.launch {
            shoppingListRepository.deleteShoppingList(list)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as LissuApplication
                HomeViewModel(app.appProvider.provideShoppingListRepository())
            }
        }
    }
}
