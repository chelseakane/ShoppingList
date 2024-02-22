package com.example.shoppinglist.models

sealed class Destination {
    data object SearchScreen : Destination()
    data object MultiListScreen : Destination()
    data class ShoppingListScreen(val listId: Int) : Destination()
}