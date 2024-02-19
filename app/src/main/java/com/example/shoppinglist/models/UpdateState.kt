package com.example.shoppinglist.models

sealed class UpdateState {
    data class Add(val item: ListItem) : UpdateState()
    data class Edit(val item: ListItemEntity) : UpdateState()
    data class Delete(val item: ListItemEntity) : UpdateState()
}