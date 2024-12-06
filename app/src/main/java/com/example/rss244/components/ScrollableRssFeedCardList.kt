package com.example.rss244.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rss244.classes.RssFeed
import com.example.rss244.classes.RssFeedViewModel

/**
 * Composable function to display a scrollable list of RSS feed cards.
 *
 * @param feeds The list of [RssFeed] objects to be displayed.
 * @param viewModel The [RssFeedViewModel] responsible for managing RSS feed actions.
 */
@Composable
fun ScrollableRssFeedCardList(feeds: List<RssFeed>, viewModel: RssFeedViewModel) {
    // LazyColumn for displaying a vertically scrollable list of RSS feeds
    LazyColumn(
        modifier = Modifier.fillMaxSize(), // Fill the entire available size
        contentPadding = PaddingValues(16.dp), // Padding around the list content
        verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items
    ) {
        // Use the `items` function to iterate through the list and create an RssFeedCard for each feed
        items(feeds) { feed ->
            RssFeedCard(feed = feed, viewModel = viewModel) // Render each feed as an RssFeedCard
        }
    }
}
