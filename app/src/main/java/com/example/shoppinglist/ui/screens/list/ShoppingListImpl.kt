package com.example.shoppinglist.ui.screens.list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.shoppinglist.R
import com.example.shoppinglist.models.DialogState
import com.example.shoppinglist.models.ListItem
import com.example.shoppinglist.models.ListItemEntity
import com.example.shoppinglist.models.ItemUpdateState
import com.example.shoppinglist.ui.components.ItemDialog
import com.example.shoppinglist.ui.theme.ShoppingListTheme

@Composable
fun ShoppingListImpl(
    list: List<ListItemEntity>,
    modifier: Modifier = Modifier,
    onUpdate: (ItemUpdateState) -> Unit,
) {
    var itemDialogState by remember { mutableStateOf<DialogState>(DialogState.Dismissed) }

    when (val state = itemDialogState) {
        is DialogState.Add -> {
            ItemDialog(
                onUpdate = { id, name, quantity ->
                    onUpdate(ItemUpdateState.Add(ListItem(id, name, quantity)))
                },
                onDismiss = { itemDialogState = DialogState.Dismissed }
            )
        }
        is DialogState.Edit -> {
            ItemDialog(
                item = state.item,
                onUpdate = { id, name, quantity ->
                    onUpdate(
                        ItemUpdateState.Edit(ListItemEntity(id, state.item.listId, name, quantity))
                    )
                },
                onDismiss = { itemDialogState = DialogState.Dismissed }
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
        if (list.isEmpty()) {
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
                    items(list) {item ->
                        ShoppingListItem(
                            name = item.name,
                            quantity = item.quantity,
                            onEdit = { itemDialogState = DialogState.Edit(item) },
                            onDelete = { onUpdate(ItemUpdateState.Delete(item)) }
                        )
                    }
                }
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            onClick = { itemDialogState = DialogState.Add },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_item))
        }
    }
}

@Composable
fun ShoppingListItem(
    name: String,
    quantity: Int?,
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
        Text(modifier = Modifier.padding(start = 8.dp), text = name)
        if (quantity != null) {
            Text(text = "Quantity: $quantity")
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
        val item1 = ListItemEntity(0, 1, "Apples", null)
        val item2 = ListItemEntity(1, 1, "Bananas", 3)
        ShoppingListImpl(
            list = listOf(item1, item2)
        ) {}
    }
}