package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.shoppinglist.models.ListItem
import com.example.shoppinglist.models.UpdateState
import com.example.shoppinglist.ui.common.SmallTopAppBar
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val items = remember { mutableStateListOf<ListItem>() }
                    var title by remember { mutableStateOf(getString(R.string.default_list_title)) }

                    SmallTopAppBar(title = title, updateTitle = { title = it }) {
                        ShoppingList(items, modifier = Modifier.padding(it)) { state ->
                            when (state) {
                                is UpdateState.Add -> items.add(state.item)
                                is UpdateState.Edit -> items[state.index] = state.item
                                is UpdateState.Delete -> items.removeAt(state.index)
                            }
                        }
                    }
                }
            }
        }
    }
}
