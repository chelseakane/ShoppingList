package com.example.shoppinglist

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.models.ItemDialogState
import com.example.shoppinglist.models.ListItem
import com.example.shoppinglist.models.UpdateState
import com.example.shoppinglist.ui.theme.ShoppingListTheme

@Composable
fun ShoppingList(
    items: List<ListItem>,
    modifier: Modifier = Modifier,
    onUpdate: (UpdateState) -> Unit
) {
    var itemDialogState by remember { mutableStateOf<ItemDialogState>(ItemDialogState.Dismissed) }

    when (val state = itemDialogState) {
        is ItemDialogState.Add -> {
            ItemDialog(
                onUpdate = { name, quantity ->
                    onUpdate(UpdateState.Add(ListItem(name, quantity)))
                },
                onDismiss = { itemDialogState = ItemDialogState.Dismissed }
            )
        }
        is ItemDialogState.Edit -> {
            ItemDialog(
                item = state.item,
                onUpdate = { name, quantity ->
                    onUpdate(UpdateState.Edit(ListItem(name, quantity), state.index))
                },
                onDismiss = { itemDialogState = ItemDialogState.Dismissed }
            )
        }
        else -> { /* No-op */ }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (items.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.empty_list_title),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Text(text = stringResource(R.string.empty_list_subtitle), textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    itemsIndexed(items) {index, item ->
                        ShoppingListItem(
                            item = item,
                            onEdit = { itemDialogState = ItemDialogState.Edit(item, index) },
                            onDelete = { onUpdate(UpdateState.Delete(index)) }
                        )
                    }
                }
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            onClick = { itemDialogState = ItemDialogState.Add },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_item))
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ListItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.padding(start = 8.dp), text = item.name)
        if (item.quantity != null) {
            Text(text = "Quantity: ${item.quantity}")
        }
        Row {
            IconButton(onClick = { onEdit() }) {
                Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit_item))
            }
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete_item))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListPreview() {
    ShoppingListTheme {
        ShoppingList(
            mutableListOf(ListItem("Apples", null), ListItem("Bananas", 3))
        ) { UpdateState.Delete(0) }
    }
}