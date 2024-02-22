package com.example.shoppinglist.models

sealed class ItemUpdateState {
    data class Add(val item: ListItem) : ItemUpdateState()
    data class Edit(val item: ListItemEntity) : ItemUpdateState()
    data class Delete(val item: ListItemEntity) : ItemUpdateState()
}