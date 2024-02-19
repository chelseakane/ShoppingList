package com.example.shoppinglist.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * `ShoppingList` is the name of the composable, whereas [ShoppingListEntity] is the object we're
 * storing in the database. Don't confuse the two.
 */
@Entity
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)
