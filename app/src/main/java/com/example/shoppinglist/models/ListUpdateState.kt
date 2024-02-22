package com.example.shoppinglist.models

sealed class ListUpdateState {
    data class Details(val id: Int) : ListUpdateState()
    data class Delete(val list: ShoppingListEntity) : ListUpdateState()
}