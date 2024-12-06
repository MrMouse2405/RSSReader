package com.example.rss244.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*

    Model

*/

/**
 * Entity class representing an RSS feed.
 *
 * @property id The unique ID of the RSS feed (auto-generated).
 * @property title The title of the RSS feed.
 * @property url The URL of the RSS feed.
 */
@Entity(tableName = "rss_feeds")
data class RssFeed(
    @PrimaryKey(autoGenerate = true) // Auto-generate unique IDs for each feed
    val id: Int = 0,
    val title: String, // Title of the RSS feed
    val url: String // URL of the RSS feed
)

/*

    Data Access Object (DAO)

*/

/**
 * DAO interface for accessing and managing RSS feed data in the database.
 */
@Dao
interface RssFeedDao {

    /**
     * Inserts a new RSS feed or updates an existing one in the database.
     *
     * @param feed The [RssFeed] to insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(feed: RssFeed)

    /**
     * Retrieves all RSS feeds from the database as a Flow.
     *
     * @return A [Flow] of a list of all [RssFeed] entries.
     */
    @Query("SELECT * FROM rss_feeds")
    fun getAllFeeds(): Flow<List<RssFeed>>

    /**
     * Deletes a specific RSS feed from the database.
     *
     * @param feed The [RssFeed] to delete.
     */
    @Delete
    suspend fun deleteFeed(feed: RssFeed)
}

/*

    ViewModel

*/

/**
 * ViewModel for managing the list of RSS feeds using the [RssFeedDao].
 *
 * @param dao The DAO for performing database operations.
 */
class RssFeedViewModel(private val dao: RssFeedDao) : ViewModel() {

    /**
     * Observes the list of all RSS feeds as a StateFlow.
     *
     * Uses [stateIn] to maintain the state while subscribed, with a timeout of 5000ms.
     */
    val feeds: StateFlow<List<RssFeed>> = dao.getAllFeeds()
        .stateIn(
            viewModelScope, // Scoped to the lifecycle of the ViewModel
            SharingStarted.WhileSubscribed(5000), // Share the Flow while there are active subscribers
            emptyList() // Initial state is an empty list
        )

    /**
     * Adds a new RSS feed to the database.
     *
     * @param title The title of the RSS feed.
     * @param url The URL of the RSS feed.
     */
    fun addFeed(title: String, url: String) {
        viewModelScope.launch {
            dao.insertFeed(
                RssFeed(
                    title = title, // Feed title
                    url = url // Feed URL
                )
            )
        }
    }

    /**
     * Deletes an existing RSS feed from the database.
     *
     * @param feed The [RssFeed] to delete.
     */
    fun deleteFeed(feed: RssFeed) {
        viewModelScope.launch {
            dao.deleteFeed(feed)
        }
    }
}
