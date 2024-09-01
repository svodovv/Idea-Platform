package com.example.ideaplatform.presentation.ui.screens

import com.example.ideaplatform.domain.model.ItemModel

data class ItemState (
    val itemList: List<ItemModel>?,
    val loading: Boolean,
    val searchText: String,
    val selectedItemAmount: Int?
){
    companion object{
        val initial = ItemState(
            itemList = null,
            loading = true,
            searchText = "",
            selectedItemAmount = null
        )
    }
}