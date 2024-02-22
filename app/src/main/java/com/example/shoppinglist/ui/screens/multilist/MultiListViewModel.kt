package com.example.shoppinglist.ui.screens.multilist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShoppingListRepository
import com.example.shoppinglist.models.ListState
import com.example.shoppinglist.models.ShoppingListEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiListViewModel @Inject constructor(
    private val repository: ShoppingListRepository
): ViewModel() {
    val lists: StateFlow<ListState<List<ShoppingListEntity>>> by lazy { _lists }
    private val _lists = MutableStateFlow<ListState<List<ShoppingListEntity>>>(ListState.Loading)

    init {
        getLists()
    }

    private fun getLists() {
        viewModelScope.launch(Dispatchers.IO) {
            _lists.value = ListState.Loaded(repository.getAllLists().first())
        }
    }

    fun addList(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addList(title)
            getLists()
        }
    }

    fun deleteList(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteList(id)
            getLists()
        }
    }
}