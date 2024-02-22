package com.example.shoppinglist.ui.screens.list

import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.R
import com.example.shoppinglist.data.ShoppingListRepository
import com.example.shoppinglist.di.ShoppingListApplication
import com.example.shoppinglist.models.ListItem
import com.example.shoppinglist.models.ListItemEntity
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.models.ShoppingListData
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
    val list: StateFlow<ListState<ShoppingListData>> by lazy { _list }
    private val _list = MutableStateFlow<ListState<ShoppingListData>>(ListState.Loading)

    var id: Int
        get() = _id
        set(value) {
            _id = value
            getListById(value)
        }
    private var _id: Int = 0

    fun getListById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = listsRepository.getListById(id).first()
            val items = listsRepository.getItems(id).first()
            _list.value = ListState.Loaded(ShoppingListData(list?.id ?: 0, list?.name, items))
        }
    }

    fun updateList(id: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            async { listsRepository.updateList(id, name) }.await()
        }
    }

    fun addItem(item: ListItem, listId: Int) {
        val defaultTitle = resources.getString(R.string.default_list_title)

        viewModelScope.launch(Dispatchers.IO) {
            listsRepository.addItem(item, listId, defaultTitle)
            getListById(listId)
        }
    }

    fun updateItem(item: ListItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            listsRepository.updateItem(item)
            getListById(item.listId)
        }
    }

    fun deleteItem(item: ListItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            listsRepository.delete(item)
            getListById(item.listId)
        }
    }
}