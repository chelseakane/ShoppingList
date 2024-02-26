package com.example.shoppinglist.models

sealed class DialogState {
    data object Add : DialogState()
    data class Delete(val id: Int) : DialogState()
    data class Edit(val item: ListItemEntity) : DialogState()
    data object Dismissed : DialogState()
}