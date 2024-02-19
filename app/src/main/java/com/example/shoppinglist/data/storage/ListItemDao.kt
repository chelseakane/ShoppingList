package com.example.shoppinglist.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.models.ListItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the list item table
 *
 * [getAll] returns all the items in the table
 * [insert] inserts one item into the table
 * [insertAll] inserts multiple items into the table (for recipes)
 * [delete] deletes one item from the table
 */
@Dao
interface ListItemDao {
    @Query("SELECT * FROM ListItemEntity")
    fun getAll(): Flow<List<ListItemEntity>>

    @Query("SELECT * FROM ListItemEntity WHERE listId = :listId")
    fun getItemsByListId(listId: Int): Flow<List<ListItemEntity>>

    @Insert
    fun insert(item: ListItemEntity)

    @Insert
    fun insertAll(vararg items: ListItemEntity)

    @Update
    fun update(item: ListItemEntity)

    @Delete
    fun delete(item: ListItemEntity)
}