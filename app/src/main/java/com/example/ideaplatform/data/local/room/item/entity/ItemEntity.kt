package com.example.ideaplatform.data.local.room.item.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ideaplatform.data.local.room.utils.ListConverters
import com.example.ideaplatform.domain.model.ItemModel

private const val tableName = "item"

@Entity(tableName = tableName)
data class ItemEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val time: Int,
    @TypeConverters(ListConverters::class)
    val tags: ArrayList<String>,
    val amount: Int

)

fun ItemEntity.toItemModel(time: String): ItemModel {
    return ItemModel(
        id = id,
        name = name,
        time = time,
        tags = tags,
        amount = amount
    )
}