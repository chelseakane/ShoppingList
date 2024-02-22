package com.example.shoppinglist.models

sealed class ListState<out T> {
    data object Loading : ListState<Nothing>()
    data class Loaded<T>(val data: T) : ListState<T>()
}