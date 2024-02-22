package com.example.shoppinglist.models

data class ShoppingListData(
    val id: Int,
    val title: String?,
    val items: List<ListItemEntity>
)
