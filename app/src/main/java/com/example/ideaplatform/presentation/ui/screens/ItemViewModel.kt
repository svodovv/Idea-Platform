package com.example.ideaplatform.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ideaplatform.domain.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
): ViewModel() {

    private val _state = MutableStateFlow(ItemState.initial)
    val state = _state.asStateFlow()

    init {
        init()
    }

    fun sendIntent(intent: ItemIntent){
        when(intent){
            is ItemIntent.DeleteItem -> deleteItem(intent)
            is ItemIntent.SearchItem -> searchItem(intent)
            is ItemIntent.UpdateItem -> updateItem(intent)
        }
    }

    private fun init(){
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.getAllItem().collectLatest { data ->
                _state.update {
                    it.copy(
                        itemList = data,
                        loading = false
                    )
                }
            }
        }
    }

    private fun deleteItem(intent: ItemIntent.DeleteItem){
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.deleteItemById(intent.itemId)
        }
    }

    private fun searchItem(intent: ItemIntent.SearchItem){

        _state.update { it.copy(searchText = intent.itemName) }

        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.getItemByName(intent.itemName).collectLatest { itemList ->
                _state.update { state ->
                    state.copy(itemList = itemList)
                }
            }
        }
    }

    private fun updateItem(intent: ItemIntent.UpdateItem){
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.updateItem(itemId = intent.itemId, itemAmount = intent.itemAmount)
        }
    }
}