package com.example.setupsheets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.setupsheets.data.NoteRepository
import com.example.setupsheets.db.Note
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * ViewModel for managing note-related operations, including search functionality.
 */
class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // Holds the current search query entered by the user
    internal val searchQuery = MutableStateFlow("")

    // All notes from the repository
    private val allNotes = repository.allNotes

    /**
     * A filtered list of notes that reacts to both the list of notes
     * and the current search query.
     */
    val notes: StateFlow<List<Note>> = combine(allNotes, searchQuery) { notes, query ->
        if (query.isBlank()) {
            notes
        } else {
            notes.filter { note ->
                note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    //Updates the current search query.
    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    //Adds a new note.
    fun addNote(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    //Updates an existing note.
    fun updateNote(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    //Deletes a note.
    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
}

/**
 * Creates instances of NoteViewModel with a repository dependency.
 */
class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}