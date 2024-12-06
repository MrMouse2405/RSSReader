package com.example.rss244.classes

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.TypeConverter
import com.example.rss244.rss.fetchRssFeed
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.random.Random

/**
 * Data class representing a News item.
 *
 * @param title Title of the news article.
 * @param source Source of the news article (e.g., website name).
 * @param postTitle Subtitle or additional title for the news article.
 * @param description Description of the news article (optional).
 * @param category List of categories or tags associated with the news article (optional).
 * @param imageURL URL of the image associated with the news article (optional).
 * @param articleURL URL of the news article (optional).
 */
@Serializable
data class News(
    val title: String,
    val source: String,
    val postTitle: String,
    val description: String?,
    val category: List<String>?,
    val imageURL: String?,
    val articleURL: String?
)

/**
 * Provides type converters for serializing and deserializing the [News] object.
 */
class NewsConverters {
    private val gson = Gson()

    /**
     * Converts a [News] object into a JSON string for storage.
     *
     * @param news The [News] object to convert.
     * @return A JSON string representation of the news.
     */
    @TypeConverter
    fun fromNews(news: News): String {
        return gson.toJson(news)
    }

    /**
     * Converts a JSON string into a [News] object.
     *
     * @param json The JSON string to deserialize.
     * @return The deserialized [News] object.
     */
    @TypeConverter
    fun toNews(json: String): News {
        val type = object : TypeToken<SavedNews>() {}.type
        return gson.fromJson(json, type)
    }
}

/**
 * Implements the Fisher-Yates shuffle algorithm to randomize a list in-place.
 *
 * @param array A mutable list of elements to shuffle.
 */
fun <T> fisherYatesShuffle(array: MutableList<T>) {
    val random = Random.Default
    for (i in array.lastIndex downTo 1) {
        val j = random.nextInt(i + 1) // Get a random index from 0 to i
        // Swap elements at indices i and j
        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }
}

/**
 * ViewModel for managing news items and loading RSS feeds.
 */
class NewsViewModel : ViewModel() {

    // Mutable list to store news items for reactive updates
    val newsItems = mutableStateListOf<News>()

    // Map to store news items fetched from specific URLs
    val urls = mutableStateMapOf<String, List<News>>()

    /**
     * Loads RSS feed data from a specified URL.
     *
     * If the URL is already loaded, the function returns early to prevent duplicate loading.
     * Otherwise, it fetches the RSS feed, stores the results in the map, and updates the
     * list of news items, shuffling them for randomness.
     *
     * @param url The URL of the RSS feed to load.
     */
    fun loadRssFeed(url: String) {
        if (urls.containsKey(url)) {
            return // If the URL is already loaded, do nothing
        }

        // Launch a coroutine to fetch the RSS feed asynchronously
        viewModelScope.launch {
            val result = fetchRssFeed(url) // Fetch RSS feed from the URL
            urls[url] = result // Cache the fetched news items
            newsItems.addAll(result) // Add the news items to the reactive list
            fisherYatesShuffle(newsItems) // Randomize the order of news items
        }
    }
}
