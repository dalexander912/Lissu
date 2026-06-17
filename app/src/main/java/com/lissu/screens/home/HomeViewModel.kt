package com.lissu.screens.home

import androidx.lifecycle.ViewModel
import com.lissu.data.models.ShoppingList
import com.lissu.data.repositories.ShoppingListRepository
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    val shoppingLists: StateFlow<List<ShoppingList>> = ShoppingListRepository.shoppingLists

    fun deleteList(list: ShoppingList) {

        ShoppingListRepository.deleteList(list.id)
    }
}
