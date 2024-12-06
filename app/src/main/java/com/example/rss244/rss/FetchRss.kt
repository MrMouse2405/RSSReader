package com.example.rss244.rss

import com.example.rss244.classes.News
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Fetches and parses an RSS feed from a given URL.
 *
 * This function uses the RssParser library to retrieve RSS feed data, processes the data, and
 * converts it into a list of [News] objects for use within the application.
 *
 * @param url The URL of the RSS feed to fetch.
 * @return A list of [News] objects representing the parsed RSS feed items.
 */
suspend fun fetchRssFeed(url: String): List<News> {
    return withContext(Dispatchers.IO) { // Perform the operation on the IO dispatcher for network calls
        val rssParser = RssParser() // Initialize the RSS parser
        val rssChannel: RssChannel = rssParser.getRssChannel(url) // Fetch the RSS channel from the URL

        val result = mutableListOf<News>() // Create a mutable list to store parsed News items

        // Iterate through the items in the RSS channel
        rssChannel.items.forEach { item ->
            result.add(
                News(
                    title = rssChannel.title.toString(), // The title of the RSS feed
                    source = url, // The source URL of the RSS feed
                    postTitle = item.title.toString(), // The title of the individual news post
                    description = item.description // Clean and format the description
                        ?.toString()
                        ?.replace(Regex("<[^>]*>"), "") // Remove HTML tags
                        ?.trim(), // Trim any leading or trailing whitespace
                    category = item.categories, // Categories associated with the post
                    imageURL = item.image, // URL of the associated image
                    articleURL = item.link // Link to the full article
                )
            )
        }

        result // Return the list of News items
    }
}
