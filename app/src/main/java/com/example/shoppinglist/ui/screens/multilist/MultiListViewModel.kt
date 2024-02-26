package com.example.shoppinglist.ui.screens.multilist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShoppingListRepository
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
class MultiListViewModel @Inject constructor(
    private val repository: ShoppingListRepository
): ViewModel() {
    val listsWithData: StateFlow<ListState<List<ShoppingListData>>> by lazy { _listsWithData }
    private val _listsWithData = MutableStateFlow<ListState<List<ShoppingListData>>>(ListState.Loading)

    val newListId: StateFlow<Int?> by lazy { _newListId }
    private val _newListId = MutableStateFlow<Int?>(null)

    init {
        getAllListsData()
    }

    fun getAllListsData() {
        viewModelScope.launch(Dispatchers.IO) {
            val listWithData: MutableList<ShoppingListData> = mutableListOf()
            for (list in repository.getAllLists().first()) {
                val items = async { repository.getItems(list.id) }.await()
                val listToAdd = ShoppingListData(list.id, list.name, items.first())
                listWithData.add(listToAdd)
            }
            _listsWithData.value = ListState.Loaded(listWithData)
        }
    }

    fun addList(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val rowId = async { repository.addList(title) }.await()
            _newListId.value = repository.getListByRowId(rowId).first()?.id
            getAllListsData()
        }
    }

    fun deleteList(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteList(id)
            getAllListsData()
        }
    }
}