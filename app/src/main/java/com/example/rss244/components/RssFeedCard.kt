package com.example.rss244.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rss244.classes.RssFeed
import com.example.rss244.classes.RssFeedViewModel

/**
 * Composable function to display an individual RSS feed card.
 *
 * @param feed The [RssFeed] object containing the details of the RSS feed.
 * @param viewModel The [RssFeedViewModel] responsible for managing feed actions like deletion.
 */
@Composable
fun RssFeedCard(feed: RssFeed, viewModel: RssFeedViewModel) {
    // Card to display the feed details
    Card(
        shape = RoundedCornerShape(12.dp), // Rounded corners for aesthetic
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer // Background color
        ),
        modifier = Modifier
            .fillMaxWidth() // Take up full width of the container
            .height(80.dp) // Fixed height for uniformity
    ) {
        // Row layout to organize content horizontally
        Row(
            verticalAlignment = Alignment.CenterVertically, // Align items vertically at the center
            horizontalArrangement = Arrangement.SpaceBetween, // Space between elements
            modifier = Modifier
                .fillMaxSize() // Fill the entire card space
                .padding(16.dp) // Add padding inside the card
        ) {
            // Row for the text and optional image
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Space between text and icon
            ) {
                Column {
                    // Display the feed title
                    Text(
                        text = feed.title, // Title of the RSS feed
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface // Text color
                    )
                    // Display the feed URL
                    Text(
                        text = feed.url, // URL of the RSS feed
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Subtle text color
                    )
                }
            }

            // Delete button to remove the feed
            IconButton(
                onClick = { viewModel.deleteFeed(feed) } // Call deleteFeed when clicked
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete, // Delete icon
                    contentDescription = "Delete", // Accessibility description
                    tint = MaterialTheme.colorScheme.onSurface // Icon color
                )
            }
        }
    }
}
