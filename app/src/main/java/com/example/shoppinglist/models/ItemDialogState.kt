package com.example.shoppinglist.models

sealed class ItemDialogState {
    data object Add : ItemDialogState()
    data class Edit(val item: ListItemEntity) : ItemDialogState()
    data object Dismissed : ItemDialogState()
}