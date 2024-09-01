package com.example.ideaplatform.domain.repository

import com.example.ideaplatform.data.local.room.item.entity.ItemEntity
import com.example.ideaplatform.domain.model.ItemModel
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    fun getItemByName(itemName: String): Flow<List<ItemModel>>
    suspend fun updateItem(itemId: Int, itemAmount: Int)
    suspend fun deleteItemById(itemId: Int)
}
