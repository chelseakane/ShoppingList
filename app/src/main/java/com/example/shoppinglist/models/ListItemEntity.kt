package com.example.shoppinglist.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = ShoppingListEntity::class,
        parentColumns = ["id"],
        childColumns = ["listId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ListItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val listId: Int, // Foreign key to the shopping list
    val name: String,
    val quantity: Int?
)
