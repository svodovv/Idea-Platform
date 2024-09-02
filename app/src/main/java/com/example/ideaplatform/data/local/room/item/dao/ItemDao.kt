package com.example.ideaplatform.data.local.room.item.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.ideaplatform.data.local.room.item.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("DELETE FROM item WHERE id = :itemId ")
    suspend fun deleteItem(itemId: Int)

    @Query("SELECT * FROM item WHERE name LIKE  :itemName ")
    fun getItemByName(itemName: String): Flow<List<ItemEntity>>

    @Query("SELECT * FROM item")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("UPDATE item SET amount = :itemAmount WHERE id = :itemId")
    suspend fun updateItem(itemId: Int, itemAmount: Int)
}