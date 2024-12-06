package com.example.rss244.classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Represents the Room database for storing RSS feeds and bookmarked news.
 *
 * @Database annotation specifies the entities and database version.
 */
@Database(entities = [RssFeed::class, SavedNews::class], version = 1)
@TypeConverters(SavedNewsConverters::class, NewsConverters::class)
abstract class RssDatabase : RoomDatabase() {

    /**
     * Provides access to the DAO for managing RSS feeds.
     *
     * @return The [RssFeedDao] instance.
     */
    abstract fun rssFeedDao(): RssFeedDao

    /**
     * Provides access to the DAO for managing bookmarked news.
     *
     * @return The [SavedNewsDao] instance.
     */
    abstract fun savedNewsDao(): SavedNewsDao
}

/**
 * Singleton object to manage the creation and access of the Room database.
 */
object Database {
    private var RssFeedDB: RssDatabase? = null

    /**
     * Retrieves the instance of the RSS database.
     *
     * If the database has not been created yet, it initializes it using the application context.
     * Ensures thread safety using `synchronized`.
     *
     * @param context The application context for building the database.
     * @return The singleton instance of [RssDatabase].
     */
    fun getRssFeedDB(context: Context): RssDatabase {
        if (RssFeedDB == null) {
            synchronized(RssDatabase::class) { // Ensure only one instance is created in multi-threaded scenarios
                RssFeedDB = Room.databaseBuilder(
                    context.applicationContext, // Use application context to avoid memory leaks
                    RssDatabase::class.java, // Specify the database class
                    "rss_database" // Set the name of the database file
                ).build()
            }
        }
        return RssFeedDB!! // Return the database instance
    }
}
