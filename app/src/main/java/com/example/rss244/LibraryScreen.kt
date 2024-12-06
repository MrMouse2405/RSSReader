package com.example.rss244

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rss244.classes.RssFeedViewModel
import com.example.rss244.components.RssFeedCard

/**
 * Composable function representing the Library screen of the application.
 *
 * @param viewModel The [RssFeedViewModel] responsible for managing the list of RSS feeds.
 * @param navController The [NavHostController] to handle navigation actions.
 */
@Composable
fun LibraryScreen(viewModel: RssFeedViewModel, navController: NavHostController) {
    // Collect the current list of RSS feeds as a state
    val feeds = viewModel.feeds.collectAsState()

    // Box layout to hold the list of RSS feeds and the FloatingActionButton
    Box(modifier = Modifier.fillMaxSize()) {
        // LazyColumn to display the list of RSS feeds
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp), // Padding around the content
            verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items
        ) {
            // Iterate through the list of feeds and display each as a RssFeedCard
            items(feeds.value) { feed ->
                RssFeedCard(feed = feed, viewModel = viewModel)
            }
        }

        // Floating Action Button to navigate to the Add RSS screen
        FloatingActionButton(
            onClick = {
                // Navigate to the Add RSS screen
                navController.navigate(NavDestination.AddRss.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true // Save state of the previous screen
                    }
                    launchSingleTop = true // Avoid duplicate instances of the screen
                    restoreState = true // Restore previous state if available
                }
            },
            modifier = Modifier
                .padding(16.dp) // Padding to position the button
                .align(Alignment.BottomEnd), // Align to the bottom-right corner
            containerColor = MaterialTheme.colorScheme.surface, // Set container color
            contentColor = MaterialTheme.colorScheme.onSurface // Set content (icon) color
        ) {
            // Icon for the Floating Action Button
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Feed")
        }
    }
}
