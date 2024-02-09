package com.example.shoppinglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.example.shoppinglist.models.ListItem

@Composable
fun ItemDialog(
    item: ListItem? = null,
    onUpdate: (String, Int?) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            var itemName by remember { mutableStateOf(item?.name ?: "") }
            var itemQuantity: Int? by remember { mutableStateOf(item?.quantity)}
            var showError: Boolean by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = "Add an item to your list",
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Enter the item(s) and quantity below.",
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )

                // text fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(3f)
                            .padding(end = 8.dp),
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Item Name") },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        isError = showError && itemName.isEmpty()
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = itemQuantity?.toString() ?: "",
                        onValueChange = {
                            itemQuantity =
                                if(it.isDigitsOnly() && it.isNotEmpty()) it.toInt() else null
                        },
                        label = { Text("#") },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                // buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                    ) {
                        Text("Dismiss")
                    }
                    Button(
                        onClick = {
                            if (itemName.isNotEmpty()) {
                                onUpdate(itemName, itemQuantity)
                                onDismiss()
                            } else {
                                showError = true
                            }
                        },
                    ) {
                        if (item != null) {
                            Text("Update Item")
                        } else {
                            Text("Add Item")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddItemDialogPreview() {
    ItemDialog(onUpdate = { _, _ ->}, onDismiss =  {})
}