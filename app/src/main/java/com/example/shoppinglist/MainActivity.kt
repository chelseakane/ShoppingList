package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.ui.components.SmallTopAppBar
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val listsViewModel: ShoppingListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val lists by listsViewModel.lists.collectAsState()
                    // if the value of lists changes, update the id
                    when (val state = lists) {
                        is ListState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is ListState.Loaded -> {
                            val id: Int by remember(lists) {
                                mutableIntStateOf(state.list.firstOrNull()?.id ?: 0)
                            }
                            val title: String by remember(id) {
                                mutableStateOf(state.list.find { it.id == id }?.name ?: getString(R.string.default_list_title))
                            }
                            if (state.list.size <= 1) {
                                SmallTopAppBar(
                                    title = title,
                                    updateTitle = {
                                        listsViewModel.updateList(id, it)
                                    }
                                ) {
                                    ShoppingList(id, modifier = Modifier.padding(it))
                                }
                            } else {
                                // TODO: show lists screen
                                // This is just for testing. If you get more than one list, you get
                                // stranded. A temporary solution is to delete the newest list.
                                Box(contentAlignment = Alignment.Center) {
                                    Button(onClick = {
                                        listsViewModel.deleteList(id)
                                    }) {
                                        Text(text = "Delete list")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
