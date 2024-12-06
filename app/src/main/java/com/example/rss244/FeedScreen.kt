package com.example.rss244

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.rss244.classes.NewsViewModel
import com.example.rss244.classes.RssFeedViewModel
import com.example.rss244.classes.SavedNewsViewModel
import com.example.rss244.components.ScrollableNewsCardList

/**
 * Composable function representing the Feed screen of the application.
 *
 * @param viewModel The [RssFeedViewModel] responsible for managing RSS feed data.
 * @param savedNewsViewModel The [SavedNewsViewModel] responsible for managing saved news data.
 */
@Composable
fun FeedScreen(viewModel: RssFeedViewModel, savedNewsViewModel: SavedNewsViewModel) {
    // Collect the current list of RSS feeds as a state
    val feeds = viewModel.feeds.collectAsState()

    // Remember a NewsViewModel instance to handle loading and managing news items
    val newsViewModel = remember { NewsViewModel() }

    // Iterate through the list of feeds and load news items from their URLs
    feeds.value.forEach { feed ->
        newsViewModel.loadRssFeed(feed.url)
    }

    // Display the news items in a scrollable list, passing the SavedNewsViewModel
    ScrollableNewsCardList(newsViewModel.newsItems, savedNewsViewModel)
}
