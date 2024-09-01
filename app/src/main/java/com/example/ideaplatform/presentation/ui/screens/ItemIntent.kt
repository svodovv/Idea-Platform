package com.example.ideaplatform.presentation.ui.screens

sealed class ItemIntent {
    data class SearchItem(val itemName: String): ItemIntent()
    data class UpdateItem(val itemId: Int, val itemAmount: Int): ItemIntent()
    data class DeleteItem(val itemId: Int): ItemIntent()
}
