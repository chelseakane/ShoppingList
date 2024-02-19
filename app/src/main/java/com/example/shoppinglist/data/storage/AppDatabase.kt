package com.example.shoppinglist.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shoppinglist.models.ListItemEntity
import com.example.shoppinglist.models.ShoppingListEntity

@Database(entities = [ListItemEntity::class, ShoppingListEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun listItemDao(): ListItemDao
}