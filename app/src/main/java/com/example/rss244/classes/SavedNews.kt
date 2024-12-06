package com.example.rss244.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/**
 * Represents a bookmarked news item to be stored in the local database.
 */
@Entity(tableName = "bookmarked_news")
@Serializable
data class SavedNews(
    @PrimaryKey(autoGenerate = true) // Auto-generate ID for each news item
    val id: Int? = 0,
    val articleUrl: String, // URL of the news article
    val title: String, // Title of the news
    val source: String, // Source of the news article
    val postTitle: String, // Subtitle or additional title for the news
    val description: String?, // Description of the news article (optional)
    val category: List<String>?, // List of categories associated with the news (optional)
    val imageURL: String? // URL of the associated image (optional)
)

/**
 * Provides type converters to store complex data types (e.g., List<String>) in the database.
 */
class SavedNewsConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromNews(news: List<String>): String {
        // Converts a list of strings into a JSON string
        return gson.toJson(news)
    }

    @TypeConverter
    fun toNews(json: String): List<String> {
        // Converts a JSON string back into a list of strings
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }
}

/**
 * Data Access Object (DAO) for interacting with the saved news database table.
 */
@Dao
interface SavedNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: SavedNews) // Inserts or updates a news item

    @Query("SELECT * FROM bookmarked_news")
    fun getAllBookmarkedArticles(): Flow<List<SavedNews>> // Retrieves all bookmarked news as a Flow

    @Delete
    suspend fun deleteNews(news: SavedNews) // Deletes a specific bookmarked news item

    @Query("DELETE FROM bookmarked_news")
    suspend fun deleteAllNews() // Deletes all bookmarked news

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_news WHERE articleURL = :articleUrl)")
    fun existsBookmarkByUrl(articleUrl: String): Flow<Boolean> // Checks if a news item is bookmarked by its URL
}

/**
 * ViewModel for managing bookmarked news items using the SavedNewsDao.
 *
 * @param dao The DAO for performing database operations.
 */
class SavedNewsViewModel(private val dao: SavedNewsDao) : ViewModel() {

    // Observes the list of all bookmarked news items as a StateFlow
    val bookmarkedNews: StateFlow<List<SavedNews>> = dao.getAllBookmarkedArticles()
        .stateIn(
            viewModelScope, // Scoped to the ViewModel lifecycle
            SharingStarted.WhileSubscribed(5000), // Shares flow while there are active subscribers
            emptyList() // Initial state is an empty list
        )

    /**
     * Adds a news item to the bookmarks.
     *
     * @param articleUrl The URL of the article to bookmark.
     * @param news The [News] object containing the details of the article.
     */
    fun addBookmark(articleUrl: String, news: News) {
        viewModelScope.launch {
            dao.insertNews(
                SavedNews(
                    articleUrl = articleUrl,
                    title = news.title,
                    source = news.source,
                    postTitle = news.postTitle,
                    description = news.description,
                    category = news.category,
                    imageURL = news.imageURL
                )
            )
        }
    }

    /**
     * Removes a news item from the bookmarks.
     *
     * @param articleUrl The URL of the article to remove from bookmarks.
     * @param news The [News] object containing the details of the article.
     */
    fun removeBookmark(articleUrl: String, news: News) {
        viewModelScope.launch {
            dao.deleteNews(
                SavedNews(
                    articleUrl = articleUrl,
                    title = news.title,
                    source = news.source,
                    postTitle = news.postTitle,
                    description = news.description,
                    category = news.category,
                    imageURL = news.imageURL
                )
            )
        }
    }

    /**
     * Clears all bookmarks from the database.
     */
    fun clearAllBookmarks() {
        viewModelScope.launch {
            dao.deleteAllNews()
        }
    }

    /**
     * Checks if a news article is bookmarked.
     *
     * @param articleUrl The URL of the article to check.
     * @return A Flow<Boolean> indicating whether the article is bookmarked.
     */
    fun isBookmarked(articleUrl: String): Flow<Boolean> {
        return dao.existsBookmarkByUrl(articleUrl)
    }
}
