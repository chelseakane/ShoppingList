package com.example.shoppinglist.models

data class Meal(
    val id: String,
    val name: String,
    val category: String,
    val instructions: String,
    val ingredients: List<String>
)
