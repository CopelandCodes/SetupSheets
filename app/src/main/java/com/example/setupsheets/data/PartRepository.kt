package com.example.setupsheets.data

import com.example.setupsheets.db.Part
import com.example.setupsheets.db.PartDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository that abstracts access to the data source for Notes.
 * Provides a clean API for accessing and manipulating notes.
 */
class PartRepository(private val partDao: PartDao) {

    // Retrieves all notes as a Flow stream from the DAO.
    val allNotes: Flow<List<Part>> = partDao.getAllNotes()

     // Inserts a new note into the database.
     // note- The note to be inserted.
    suspend fun insert(part: Part) {
        partDao.insert(part)
    }

    // Updates an existing note in the database.
    // note- The note with updated values.
    suspend fun update(part: Part) {
        partDao.update(part)
    }

    // Deletes a note from the database.
    // note- The note to be deleted.
    suspend fun delete(part: Part) {
        partDao.delete(part)
    }
}
