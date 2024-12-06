package com.example.rss244.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.rss244.classes.News
import com.example.rss244.classes.SavedNewsViewModel

/**
 * Composable function to display a news item card.
 *
 * @param news The [News] object containing the details of the news article.
 * @param bookmarksViewModel The [SavedNewsViewModel] responsible for managing bookmarked articles.
 */
@Composable
fun NewsCard(news: News, bookmarksViewModel: SavedNewsViewModel) {
    val context = LocalContext.current

    // Collect the bookmark state for the current news article
    val isBookMarked =
        bookmarksViewModel.isBookmarked(news.articleURL ?: "").collectAsState(initial = false)

    // Card layout to display the news details
    Card(
        shape = RoundedCornerShape(16.dp), // Rounded corners for aesthetics
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer // Background color
        ),
        modifier = Modifier
            .fillMaxWidth() // Card spans the full width
            .padding(4.dp) // Padding around the card
            .clickable(
                enabled = !news.articleURL.isNullOrBlank(), // Enable click only if URL exists
                onClick = {
                    news.articleURL?.let {
                        try {
                            // Open the news article URL in a browser
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.articleURL))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Show a toast message if the URL cannot be opened
                            Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
    ) {
        // Column layout to structure the card content
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Space between elements
        ) {
            // Title row with the news title and bookmark icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // News title
                Text(
                    text = news.title, // Title of the news article
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold, // Bold font for emphasis
                    color = MaterialTheme.colorScheme.onSurface // Text color
                )

                // Bookmark button
                news.articleURL?.let {
                    if (isBookMarked.value) {
                        // If already bookmarked, show the "Remove Bookmark" icon
                        IconButton(onClick = {
                            bookmarksViewModel.removeBookmark(news.articleURL, news) // Remove bookmark
                        }) {
                            Icon(
                                Icons.Outlined.Bookmark, // Filled bookmark icon
                                contentDescription = "Remove Bookmark Icon",
                                modifier = Modifier.size(20.dp), // Icon size
                                tint = MaterialTheme.colorScheme.onSurfaceVariant // Icon color
                            )
                        }
                    } else {
                        // If not bookmarked, show the "Add Bookmark" icon
                        IconButton(onClick = {
                            bookmarksViewModel.addBookmark(news.articleURL, news) // Add bookmark
                        }) {
                            Icon(
                                Icons.Outlined.BookmarkBorder, // Outlined bookmark icon
                                contentDescription = "Add Bookmark Icon",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // News source
            Text(
                text = news.source, // Source of the news article
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp)) // Spacer for spacing between elements

            // News image
            news.imageURL?.let {
                AsyncImage(
                    model = news.imageURL, // URL of the news image
                    contentDescription = "Feed Article Image",
                    contentScale = ContentScale.Crop, // Crop the image to fit
                    modifier = Modifier.fillMaxWidth() // Image spans the full width
                )
            }

            // Post title
            Text(
                text = news.postTitle, // Post title of the news
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // News description
            news.description?.let {
                Text(
                    text = news.description, // Description of the news article
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Spacer for spacing before the category

            // News category
            news.category?.let {
                Text(
                    text = "# ${news.category}", // Display category as a hashtag
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary // Highlight with primary color
                )
            }
        }
    }
}