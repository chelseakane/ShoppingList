package com.example.shoppinglist.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.models.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

/**
 * CRUD operations for the shopping list table
 */
@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM ShoppingListEntity")
    fun getAll(): Flow<List<ShoppingListEntity>>

    @Query("SELECT * FROM ShoppingListEntity WHERE id = :id")
    fun getById(id: Int): Flow<ShoppingListEntity?>

    @Insert
    fun insert(list: ShoppingListEntity): Long

    @Query("SELECT * FROM ShoppingListEntity WHERE rowid = :rowId")
    fun getByIdFromRowId(rowId: Long): Flow<ShoppingListEntity?>

    @Update
    fun update(list: ShoppingListEntity)

    @Query("DELETE FROM ShoppingListEntity WHERE id = :id")
    fun delete(id: Int)
}