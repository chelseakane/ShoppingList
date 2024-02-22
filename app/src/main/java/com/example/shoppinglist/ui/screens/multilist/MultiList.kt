package com.example.shoppinglist.ui.screens.multilist

import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppinglist.R
import com.example.shoppinglist.models.Destination
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.models.ListUpdateState
import com.example.shoppinglist.models.ShoppingListEntity

@Composable
fun MultiList(
    modifier: Modifier = Modifier,
    onNavigate: (Destination) -> Unit
) {
    val multiListViewModel: MultiListViewModel = hiltViewModel()
    val lists = multiListViewModel.lists.collectAsState()

    when (val state = lists.value) {
        is ListState.Loading -> {
            // show loading indicator
        }
        is ListState.Loaded -> {
            MultiListImpl(lists = state.data, modifier = modifier, onNavigate = onNavigate)
        }
    }
}

@Composable
fun MultiListImpl(
    lists: List<ShoppingListEntity>,
    modifier: Modifier = Modifier,
    onNavigate: (Destination) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.padding(16.dp),
            content = {
                items(lists) { list ->
                    ListContainer(list.id) {
                        when (it) {
                            is ListUpdateState.Details -> {
                                onNavigate(Destination.ShoppingListScreen(it.id))
                            }

                            is ListUpdateState.Delete -> {
                                // show dialog to confirm delete
                                // delete in vm on confirmation
                                // refresh list
                            }
                        }
                    }
                }
            }
        )
        val context = LocalContext.current
        FloatingActionButton(
            // open dialog with textField to add name and submit or cancel buttons
            // on submit create new list and navigate to it
            onClick = { Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT).show() },
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
    val lists = listOf(
        ShoppingListEntity(1, "List 1"),
        ShoppingListEntity(2, "List 2"),
        ShoppingListEntity(3, "List 3"),
        ShoppingListEntity(4, "List 4"),
        ShoppingListEntity(5, "List 5"),
        ShoppingListEntity(6, "List 6"),
        ShoppingListEntity(7, "List 7"),
        ShoppingListEntity(8, "List 8"),
        ShoppingListEntity(9, "List 9"),
        ShoppingListEntity(10, "List 10"),
    )
    MultiListImpl(lists = lists, onNavigate = {})
}