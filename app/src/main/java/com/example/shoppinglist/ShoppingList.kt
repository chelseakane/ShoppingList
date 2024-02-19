package com.example.shoppinglist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.models.UpdateState

/**
 * To make showing a preview easier, we get the list here and then pass it to [ShoppingListImpl]
 */
@Composable
fun ShoppingList(
    listId: Int,
    modifier: Modifier = Modifier
) {
    val shoppingListViewModel: ShoppingListViewModel = viewModel()
    val items = shoppingListViewModel.items.collectAsState()

    LaunchedEffect(listId) {
        shoppingListViewModel.getItems(listId)
    }

    when (val state = items.value) {
        is ListState.Loading -> {
            // we're already showing a loading indicator in main, so don't add flicker with another
        }
        is ListState.Loaded -> {
            ShoppingListImpl(list = state.list, modifier = modifier) {
                when (it) {
                    is UpdateState.Add -> {
                        shoppingListViewModel.addItem(it.item, listId)
                    }
                    is UpdateState.Edit -> {
                        shoppingListViewModel.updateItem(it.item)
                    }
                    is UpdateState.Delete -> {
                        shoppingListViewModel.deleteItem(it.item)
                    }
                }
            }
        }
    }
}