package com.example.setupsheets.data

import com.example.setupsheets.db.Note
import com.example.setupsheets.db.NoteDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository that abstracts access to the data source for Notes.
 * Provides a clean API for accessing and manipulating notes.
 */
class NoteRepository(private val noteDao: NoteDao) {

    // Retrieves all notes as a Flow stream from the DAO.
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

     // Inserts a new note into the database.
     // note- The note to be inserted.
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    // Updates an existing note in the database.
    // note- The note with updated values.
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    // Deletes a note from the database.
    // note- The note to be deleted.
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}
