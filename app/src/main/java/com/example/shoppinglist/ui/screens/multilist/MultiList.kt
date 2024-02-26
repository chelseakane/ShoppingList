package com.example.shoppinglist.ui.screens.multilist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppinglist.R
import com.example.shoppinglist.models.Destination
import com.example.shoppinglist.models.DialogState
import com.example.shoppinglist.models.ListItemEntity
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.models.ListUpdateState
import com.example.shoppinglist.models.ShoppingListData
import com.example.shoppinglist.ui.components.ConfirmationDialog
import com.example.shoppinglist.ui.components.ListDialog

@Composable
fun MultiList(
    modifier: Modifier = Modifier,
    onNavigate: (Destination) -> Unit
) {
    val multiListViewModel: MultiListViewModel = hiltViewModel()
    val lists = multiListViewModel.listsWithData.collectAsState()
    val newListId = multiListViewModel.newListId.collectAsState()

    LaunchedEffect(newListId.value) {
        newListId.value?.let { id ->
            onNavigate(Destination.ShoppingListScreen(id))
        }
    }

    when (val state = lists.value) {
        is ListState.Loading -> {
            // show loading indicator
        }
        is ListState.Loaded -> {
            MultiListImpl(
                lists = state.data,
                modifier = modifier,
                onNavigate = onNavigate,
                onDelete = { id ->
                    multiListViewModel.deleteList(id)
                },
                onAdd = { title ->
                    multiListViewModel.addList(title)
                }
            )
        }
    }
}

@Composable
fun MultiListImpl(
    lists: List<ShoppingListData>,
    modifier: Modifier = Modifier,
    onNavigate: (Destination) -> Unit,
    onDelete: (Int) -> Unit,
    onAdd: (String) -> Unit
) {
    var createDialogState by remember { mutableStateOf<DialogState>(DialogState.Dismissed) }
    var deleteDialogState by remember { mutableStateOf<DialogState>(DialogState.Dismissed) }

    if (createDialogState == DialogState.Add) {
        ListDialog(
            onDismiss = { createDialogState = DialogState.Dismissed }
        ) {
            onAdd(it)
        }
    }

    if (deleteDialogState is DialogState.Delete) {
        ConfirmationDialog(
            onDismiss = { deleteDialogState = DialogState.Dismissed }
        ) {
            onDelete((deleteDialogState as DialogState.Delete).id)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            content = {
                items(lists) { list ->
                    ListContainer(list.id) {
                        when (it) {
                            is ListUpdateState.Details -> {
                                onNavigate(Destination.ShoppingListScreen(it.id))
                            }

                            is ListUpdateState.Delete -> {
                                deleteDialogState = DialogState.Delete(it.id)
                            }
                        }
                    }
                }
            }
        )
        FloatingActionButton(
            onClick = { createDialogState = DialogState.Add },
            modifier = Modifier.align(Alignment.BottomEnd),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_list))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultiListImplPreview() {
    val items = listOf(ListItemEntity(1, 1, "Apples", null), ListItemEntity(2, 1, "Bananas", 3), ListItemEntity(3, 1, "Oranges", 5))
    val lists = listOf(
        ShoppingListData(1, "List 1", items),
        ShoppingListData(2, "List 2", emptyList()),
        ShoppingListData(3, "List 3", emptyList()),
        ShoppingListData(4, "List 4", emptyList()),
        ShoppingListData(5, "List 5", emptyList()),
        ShoppingListData(6, "List 6", emptyList()),
        ShoppingListData(7, "List 7", emptyList()),
        ShoppingListData(8, "List 8", emptyList()),
        ShoppingListData(9, "List 9", emptyList()),
        ShoppingListData(10, "List 10", emptyList()),
    )
    MultiListImpl(lists = lists, onNavigate = {}, onAdd = {}, onDelete = {})
}