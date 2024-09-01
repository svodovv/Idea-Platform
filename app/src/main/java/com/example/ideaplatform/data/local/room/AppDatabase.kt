package com.example.ideaplatform.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ideaplatform.data.local.room.item.dao.ItemDao
import com.example.ideaplatform.data.local.room.item.entity.ItemEntity
import com.example.ideaplatform.data.local.room.utils.ListConverters

@Database(entities = [ItemEntity::class], version = 1, exportSchema = false)
@TypeConverters( ListConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

}