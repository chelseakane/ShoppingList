package com.example.shoppinglist.ui.screens.list

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppinglist.R
import com.example.shoppinglist.models.Destination
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.models.ItemUpdateState
import com.example.shoppinglist.ui.components.SmallTopAppBar

/**
 * To make showing a preview easier, we get the list here and then pass it to [ShoppingListImpl]
 */
@Composable
fun ShoppingList(
    listId: Int,
    onNavigate: (Destination) -> Unit
) {
    val shoppingListViewModel: ShoppingListViewModel = hiltViewModel()
    shoppingListViewModel.id = listId
    val list = shoppingListViewModel.list.collectAsState()

    LaunchedEffect(listId) {
        shoppingListViewModel.getListById(listId)
    }

    when (val state = list.value) {
        is ListState.Loading -> {
            // we're already showing a loading indicator in main, so don't add flicker with another
        }
        is ListState.Loaded -> {
            SmallTopAppBar(
                title = state.data.title ?: stringResource(id = R.string.default_list_title),
                updateTitle = {
                    shoppingListViewModel.updateList(listId, it)
                },
                onNavigate = onNavigate
            ) { innerPadding ->
                ShoppingListImpl(
                    list = state.data.items,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    when (it) {
                        is ItemUpdateState.Add -> {
                            shoppingListViewModel.addItem(it.item, listId)
                        }

                        is ItemUpdateState.Edit -> {
                            shoppingListViewModel.updateItem(it.item)
                        }

                        is ItemUpdateState.Delete -> {
                            shoppingListViewModel.deleteItem(it.item)
                        }
                    }
                }
            }
        }
    }
}