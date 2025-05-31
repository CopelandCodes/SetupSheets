package com.example.setupsheets.db.converters

import androidx.room.TypeConverter
import com.example.setupsheets.db.Tool
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * A Room TypeConverter for converting a List<Tool> to and from a JSON string.
 *
 * This allows Room to store complex data structures in a single database field
 * by serializing and deserializing using Gson.
 * This is needed to handle the tool number and tool description fields
 */
class ToolListConverter {

    // Gson instance used for JSON serialization and deserialization.
    private val gson = Gson()

    /**
     * Converts a List<Tool> to a JSON string so that it can be stored as a single field in the database.
     *
     * value- The list of Tool objects to convert.
     * Returns a JSON string representation of the list.
     */
    @TypeConverter
    fun fromToolList(value: List<Tool>?): String {
        return gson.toJson(value)
    }

    /**
     * Converts a JSON string from the database back to a List<Tool>.
     *
     * value- The JSON string representation of a list of Tool objects.
     * Returns a List<Tool> reconstructed from the JSON string.
     */
    @TypeConverter
    fun toToolList(value: String): List<Tool> {
        val type = object : TypeToken<List<Tool>>() {}.type
        return gson.fromJson(value, type)
    }
}
