package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shoppinglist.models.Destination
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.ui.screens.list.ShoppingList
import com.example.shoppinglist.ui.screens.multilist.MultiList
import com.example.shoppinglist.ui.screens.multilist.MultiListViewModel
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShoppingListTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "start") {
                    composable("start") {
                        MainContent {
                            when (it) {
                                is Destination.ShoppingListScreen ->
                                    navController.navigate("shoppingList/${it.listId}")

                                is Destination.MultiListScreen ->
                                    navController.navigate("multiList")

                                is Destination.SearchScreen -> {
                                    // not implemented
                                }
                            }
                        }
                    }
                    composable(
                        "shoppingList/{listId}",
                        arguments = listOf(navArgument("listId") { type = NavType.IntType })
                    ) {
                        val listId = it.arguments?.getInt("listId") ?: 0
                        ShoppingList(listId) { destination ->
                            when (destination) {
                                is Destination.MultiListScreen -> {
                                    navController.navigate("multiList")
                                }
                                else -> {/* no-op */}
                            }
                        }
                    }
                    composable("multiList") {
                        MultiList {
                            when (it) {
                                is Destination.ShoppingListScreen -> {
                                    navController.navigate("shoppingList/${it.listId}")
                                }
                                else -> {/* no-op */}
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(
    onNavigate: (Destination) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val listsViewModel: MultiListViewModel = hiltViewModel()
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
                    mutableIntStateOf(state.data.firstOrNull()?.id ?: 0)
                }
                if (state.data.size <= 1) {
                    ShoppingList(id) {
                        onNavigate(it)
                    }
                } else {
                    MultiList {
                        onNavigate(it)
                    }
                }
            }
        }
    }
}
