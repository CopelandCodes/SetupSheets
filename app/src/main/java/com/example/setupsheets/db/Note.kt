package com.example.setupsheets.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.setupsheets.db.converters.ToolListConverter

/**
 * Entity representing a Note with all required fields for machining setup sheets.
 */
@Entity(tableName = "notes")
@TypeConverters(ToolListConverter::class) // Required to store list of custom Tool objects
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Required: Part name
    val title: String,

    // Required: Coordinates in format "X:val Y:val Z:val"
    val coordinates: String,

    // Optional: Notes field
    val content: String,

    // Required: List of tools used in main spindle
    val mainSpindleTools: List<Tool>,

    // Optional: List of tools used in sub spindle
    val subSpindleTools: List<Tool>,

    // Required: Decimal number as string (e.g., "120.5")
    val projectionLength: String,

    // Required: Decimal number as string (e.g., "1.25")
    val barSize: String,

    // Optional: Decimal number as string (e.g., "16C" or "1.00")
    val subSpindleColletSize: String
)
