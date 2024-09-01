package com.example.ideaplatform.data.local.room.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverters {
    private val gson = Gson()
    private val type = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun fromList(list: ArrayList<String>): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(json: String?): ArrayList<String> {
        return gson.fromJson(json, type)

    }
}