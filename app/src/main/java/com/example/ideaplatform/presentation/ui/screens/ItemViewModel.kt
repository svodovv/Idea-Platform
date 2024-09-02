package com.example.ideaplatform.presentation.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ideaplatform.domain.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ItemState.initial)
    val state = _state.asStateFlow()


    init {
        init()
    }

    fun sendIntent(intent: ItemIntent) {
        when (intent) {
            is ItemIntent.DeleteItem -> deleteItem(intent)
            is ItemIntent.SearchItem -> searchItem(intent.itemName)
            is ItemIntent.UpdateItem -> updateItem(intent)
        }
    }

    private fun deleteItem(intent: ItemIntent.DeleteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.deleteItemById(intent.itemId)
        }
    }

    private fun init(){
        viewModelScope.launch(Dispatchers.IO) {
            val listItems = itemRepository.getAllItems().first()
            _state.update { state ->
                state.copy(itemList = listItems)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun searchItem(searchText: String) {

        _state.update { it.copy(searchText = searchText) }

        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.getItemByName(state.value.searchText)
                .debounce(200)
                .distinctUntilChanged()
                .collect { itemList ->
                    _state.update { state ->

                        val newList = itemList.filter { it.name.contains(state.searchText, true) }
                        state.copy(itemList = newList)
                    }
                }
        }
    }

    private fun updateItem(intent: ItemIntent.UpdateItem) {
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.updateItem(itemId = intent.itemId, itemAmount = intent.itemAmount)
        }
    }
}