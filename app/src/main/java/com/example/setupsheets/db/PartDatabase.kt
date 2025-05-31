// data/NoteDatabase.kt
package com.example.setupsheets.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.setupsheets.db.converters.ToolListConverter

/**
 * Defines the Room Database for storing Notes.
 * Uses a TypeConverter to handle lists of Tool objects.
 */
@Database(entities = [Part::class], version = 2, exportSchema = false)
@TypeConverters(ToolListConverter::class)
abstract class PartDatabase : RoomDatabase() {

    /**
     * Provides access to the Data Access Object (DAO) for Note operations.
     * Room generates the implementation at compile time.
     */
    abstract fun noteDao(): PartDao

    companion object {
        // Holds an instance of the database to avoid having multiple open connections.
        @Volatile private var INSTANCE: PartDatabase? = null

        /**
         * Returns an instance of the database.
         * If the database doesn't exist, it is created using the application context.
         * Uses Room's fallbackToDestructiveMigration() for automatic schema migrations,
         * but this will clear all data on version changes.
         *
         * @param context The application context used to create or access the database.
         * @return The single instance of the NoteDatabase.
         */
        fun getDatabase(context: Context): PartDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    PartDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
