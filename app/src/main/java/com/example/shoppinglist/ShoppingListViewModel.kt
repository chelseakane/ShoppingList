package com.example.shoppinglist

import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShoppingListRepository
import com.example.shoppinglist.di.ShoppingListApplication
import com.example.shoppinglist.models.ListItem
import com.example.shoppinglist.models.ListItemEntity
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.models.ShoppingListEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val listsRepository: ShoppingListRepository,
    private val resources: Resources
) : AndroidViewModel(application = ShoppingListApplication()) {
    val lists: StateFlow<ListState<ShoppingListEntity>> by lazy { _lists }
    private val _lists = MutableStateFlow<ListState<ShoppingListEntity>>(ListState.Loading)

    val items: StateFlow<ListState<ListItemEntity>> by lazy { _items }
    private val _items = MutableStateFlow<ListState<ListItemEntity>>(ListState.Loading)

    init {
        getLists()
    }

    private fun getLists() {
        viewModelScope.launch(Dispatchers.IO) {
            _lists.value = ListState.Loaded(listsRepository.getAllLists().first())
        }
    }

    fun updateList(id: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            async { listsRepository.updateList(id, name) }.await()
            getLists()
        }
    }

    fun deleteList(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            async { listsRepository.deleteList(id) }.await()
            getLists()
        }
    }

    fun addItem(item: ListItem, listId: Int) {
        val defaultTitle = resources.getString(R.string.default_list_title)

        viewModelScope.launch(Dispatchers.IO) {
            listsRepository.addItem(item, listId, defaultTitle)
            getItems(listId)
            getLists()
        }
    }

    suspend fun getItems(listId: Int) {
        _items.value = ListState.Loaded(listsRepository.getItems(listId).first())
    }

    fun updateItem(item: ListItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            listsRepository.updateItem(item)
            getItems(item.listId)
        }
    }

    fun deleteItem(item: ListItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            listsRepository.delete(item)
            getItems(item.listId)
        }
    }
}