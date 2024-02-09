package com.example.shoppinglist.models

sealed class UpdateState {
    data class Add(val item: ListItem) : UpdateState()
    data class Edit(val item: ListItem, val index: Int) : UpdateState()
    data class Delete(val index: Int) : UpdateState()
}