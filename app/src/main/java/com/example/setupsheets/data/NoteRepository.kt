package com.example.setupsheets.data

import com.example.setupsheets.db.Note
import com.example.setupsheets.db.NoteDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to data sources for notes.
 * It provides a clean API for the ViewModel to interact with the Room database.
 */
class NoteRepository(private val noteDao: NoteDao) {

    /**
     * A flow of all notes in the database, ordered by ID descending.
     */
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    /**
     * Inserts a new note into the database.
     *
     * @param note The note to insert.
     */
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    /**
     * Updates an existing note in the database.
     *
     * @param note The note to update.
     */
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    /**
     * Deletes a note from the database.
     *
     * @param note The note to delete.
     */
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    /**
     * Retrieves a note by its ID.
     *
     * @param id The ID of the note.
     * @return The note with the specified ID, or null if not found.
     */
    suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    /**
     * Searches for notes that contain the given query in their title or content.
     *
     * @param query The text to search for.
     * @return A flow of notes matching the search criteria.
     */
    fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes(query)
    }
}
