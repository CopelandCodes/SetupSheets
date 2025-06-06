package com.example.setupsheets.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the Note entity.
 */
@Dao
interface PartDao {

    /**
     * Inserts a new note into the database.
     * If there's a conflict on the primary key, the new data replaces the old.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(part: Part)

    // Updates an existing note.
    @Update
    suspend fun update(part: Part)

    // Deletes a specific note.
    @Delete
    suspend fun delete(part: Part)

    // Returns a flow list of all notes ordered by ID descending.
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Part>>

    // Returns a single note by its ID.
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Part?

    // Searches notes whose title or content contains the provided keyword (case-insensitive).
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY id DESC")
    fun searchNotes(query: String): Flow<List<Part>>
}
