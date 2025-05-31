package com.example.setupsheets.db

/**
 * Entity representing a single tool with name and description.
 * Used to hold the tool information for the main and sub-spindle tools.
 */
data class Tool(
    val name: String,
    val description: String
)
