package com.example.shoppinglist.ui.components

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.example.shoppinglist.R
import com.example.shoppinglist.models.ListItemEntity

@Composable
fun ItemDialog(
    item: ListItemEntity? = null,
    onUpdate: (Int, String, Int?) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            // an id of 0 will be ignored by Room and replaced with a unique id
            val itemId: Int by remember { mutableIntStateOf(item?.id ?: 0) }
            var itemName by remember { mutableStateOf(item?.name ?: "") }
            var itemQuantity: Int? by remember { mutableStateOf(item?.quantity)}
            var showError: Boolean by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.add_item_title),
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(id = R.string.add_item_subtitle),
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
                        label = { Text(stringResource(R.string.item_name)) },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        isError = showError && itemName.isEmpty(),
                        supportingText = {
                            if (showError && itemName.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.add_item_error),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
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
                        Text(stringResource(R.string.dismiss))
                    }
                    Button(
                        onClick = {
                            if (itemName.isNotEmpty()) {
                                onUpdate(itemId, itemName, itemQuantity)
                                onDismiss()
                            } else {
                                showError = true
                            }
                        },
                    ) {
                        if (item != null) {
                            Text(stringResource(R.string.update_item))
                        } else {
                            Text(stringResource(R.string.add_item))
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
    ItemDialog(onUpdate = { _, _, _ ->}, onDismiss =  {})
}