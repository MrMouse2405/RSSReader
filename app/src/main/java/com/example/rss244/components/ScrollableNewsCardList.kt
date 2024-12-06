package com.example.rss244.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rss244.classes.News
import com.example.rss244.classes.SavedNewsViewModel

/**
 * Composable function to display a scrollable list of news cards.
 *
 * @param items The list of [News] objects to be displayed.
 * @param bookmarksViewModel The [SavedNewsViewModel] responsible for managing bookmarks.
 * @param modifier Modifier to apply customizations to the LazyColumn.
 */
@Composable
fun ScrollableNewsCardList(
    items: List<News>,
    bookmarksViewModel: SavedNewsViewModel,
    modifier: Modifier = Modifier
) {
    // LazyColumn for displaying a vertically scrollable list of items
    LazyColumn(
        modifier = modifier.fillMaxSize(), // Fill the entire available size
        contentPadding = PaddingValues(8.dp), // Padding around the content
        verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between items
    ) {
        // Use the `items` function to iterate through the list and create a NewsCard for each item
        items(items) { item ->
            NewsCard(item, bookmarksViewModel) // Display each news item as a NewsCard
        }
    }
}
