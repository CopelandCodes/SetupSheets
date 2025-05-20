// data/Note.kt
package com.example.setupsheets.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val coordinates: String,
    val content: String,
    val toolList: String,
    val projectionLength: String,
    val barSize: String,
    val subSpindleColletSize: String
)
