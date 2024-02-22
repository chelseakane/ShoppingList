package com.example.shoppinglist.data

import com.example.shoppinglist.data.storage.AppDatabase
import com.example.shoppinglist.models.ListItem
import com.example.shoppinglist.models.ListItemEntity
import com.example.shoppinglist.models.ShoppingListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val db: AppDatabase
) {
    /**
     * Will search the database for any shopping lists and return them if found
     */
    fun getAllLists(): Flow<List<ShoppingListEntity>> {
        return db.shoppingListDao().getAll()
    }

    fun getListById(id: Int): Flow<ShoppingListEntity?> {
        return db.shoppingListDao().getById(id)
    }

    /**
     * Adds a new list to the database
     */
    fun addList(title: String) {
        db.shoppingListDao().insert(ShoppingListEntity(0, title))
    }

    /**
     * Updates the given list with a new name
     */
    fun updateList(id: Int, name: String) {
        db.shoppingListDao().update(ShoppingListEntity(id, name))
    }

    /**
     * Deletes the given list by id
     */
    fun deleteList(id: Int) {
        db.shoppingListDao().delete(id)
    }

    /**
     * Adds an item to a given list. If the list does not exist, one will be created and then the
     * item will be added.
     */
    suspend fun addItem(
        item: ListItem,
        listId: Int,
        title: String
    ) {
        var targetList: ShoppingListEntity?

        // check if the list exists
        targetList = db.shoppingListDao().getById(listId).first()

        if (targetList == null) {
            // create a new list and fetch again, so we can add items to it
            db.shoppingListDao().insert(ShoppingListEntity(0, title))
            targetList = db.shoppingListDao().getById(listId).first()
        }

        // either way we have a list now, so add the item
        if (targetList != null) {
            val itemEntity = ListItemEntity(0, targetList.id, item.name, item.quantity)
            db.listItemDao().insert(itemEntity)
        }
    }

    /**
     * Fetches all items for a given list by list id
     */
    fun getItems(listId: Int) : Flow<List<ListItemEntity>> {
        return db.listItemDao().getItemsByListId(listId)
    }

    /**
     * Updates the given item
     */
    fun updateItem(item: ListItemEntity) {
        db.listItemDao().update(item)
    }

    /**
     * Deletes the given item
     */
    fun delete(item: ListItemEntity) {
        db.listItemDao().delete(item)
    }
}