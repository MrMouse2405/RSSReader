package com.example.rss244

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.rss244.classes.News
import com.example.rss244.classes.NewsViewModel
import com.example.rss244.classes.SavedNewsViewModel
import com.example.rss244.components.ScrollableNewsCardList

/**
 * Composable function representing the Saved screen of the application.
 *
 * @param viewModel The [SavedNewsViewModel] responsible for managing saved news data.
 */
@Composable
fun SavedScreen(viewModel: SavedNewsViewModel) {
    // Collect the current list of bookmarked news as a state
    val feeds = viewModel.bookmarkedNews.collectAsState()

    // Create a NewsViewModel instance to manage and display the saved news items
    val newsViewModel = NewsViewModel()

    // Convert each saved news item into a News object and add it to the news items list
    feeds.value.forEach { saved ->
        newsViewModel.newsItems.add(
            News(
                title = saved.title.toString(), // Convert title to string
                source = saved.source.toString(), // Convert source to string
                postTitle = saved.postTitle.toString(), // Convert post title to string
                description = saved.description, // Directly use the description
                category = saved.category, // Directly use the category
                imageURL = saved.imageURL, // URL for the news image
                articleURL = saved.articleUrl // URL for the full article
            )
        )
    }

    // Display the saved news items in a scrollable list
    ScrollableNewsCardList(newsViewModel.newsItems, viewModel)
}
