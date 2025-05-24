package com.example.setupsheets.db.converters

import androidx.room.TypeConverter
import com.example.setupsheets.db.Tool
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Converts List<Tool> to JSON and back for Room database storage.
 */
class ToolListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromToolList(value: List<Tool>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toToolList(value: String): List<Tool> {
        val type = object : TypeToken<List<Tool>>() {}.type
        return gson.fromJson(value, type)
    }
}
