package com.lissu.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lissu.LissuApplication
import com.lissu.data.models.ShoppingList
import com.lissu.data.repositories.ShoppingListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortOrder {
    NAME_ASC,
    NAME_DESC,
    NEWEST,
    OLDEST
}

class HomeViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    val shoppingLists: StateFlow<List<ShoppingList>> = 
        shoppingListRepository.getShoppingListsWithItems()
            .combine(_sortOrder) { lists, order ->
                when (order) {
                    SortOrder.NAME_ASC -> lists.sortedBy { it.name.lowercase() }
                    SortOrder.NAME_DESC -> lists.sortedByDescending { it.name.lowercase() }
                    SortOrder.NEWEST -> lists.reversed()
                    SortOrder.OLDEST -> lists
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun changeSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

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
