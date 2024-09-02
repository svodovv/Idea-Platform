package com.example.ideaplatform.data.repository

import android.annotation.SuppressLint
import com.example.ideaplatform.data.local.room.item.dao.ItemDao
import com.example.ideaplatform.data.local.room.item.entity.toItemModel
import com.example.ideaplatform.domain.model.ItemModel
import com.example.ideaplatform.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemDao: ItemDao
) : ItemRepository {


    override fun getItemByName(itemName: String): Flow<List<ItemModel>> {
        return itemDao.getItemByName(itemName = "%$itemName%").map { entity ->
            entity.map { item ->
                val time = convertUnixTimeToString(item.time)
                item.toItemModel(time)
            }
        }
    }

    override suspend fun updateItem(itemId: Int, itemAmount: Int) {
        itemDao.updateItem(itemId = itemId, itemAmount = itemAmount)
    }

    override suspend fun getAllItems(): Flow<List<ItemModel>> {
        return itemDao.getAllItems().map { entity ->
            entity.map { item ->
                val time = convertUnixTimeToString(item.time)
                item.toItemModel(time)
            }
        }
    }

    override suspend fun deleteItemById(itemId: Int) {
        itemDao.deleteItem(itemId = itemId)
    }

    @SuppressLint("NewApi")
    private fun convertUnixTimeToString(unixTime: Int): String {
        val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime.toLong()), ZoneId.systemDefault())

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return dateTime.format(formatter)

    }


}
