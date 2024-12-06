package com.example.rss244

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rss244.classes.RssFeedViewModel

/**
 * Composable function representing the screen for adding a new RSS feed.
 *
 * @param viewModel The [RssFeedViewModel] responsible for managing RSS feed data.
 * @param navController The [NavHostController] to handle navigation actions.
 */
@Composable
fun AddRssScreen(viewModel: RssFeedViewModel, navController: NavHostController) {
    // Mutable states to hold feed name, feed URL, and validation flags
    var feedName = remember { mutableStateOf("") }
    var feedUrl = remember { mutableStateOf("") }
    var isUrlValid = remember { mutableStateOf(true) }
    var isNameValid = remember { mutableStateOf(true) }

    // Function to validate the RSS feed URL
    fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    // Function to validate the feed name (non-empty)
    fun isValidName(name: String): Boolean {
        return name.isNotEmpty()
    }

    // Column layout for organizing the form elements
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input field for the feed name
        OutlinedTextField(
            value = feedName.value,
            onValueChange = {
                feedName.value = it
                isNameValid.value = isValidName(it) // Validate the feed name
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isNameValid.value // Highlight field if invalid
        )

        // Error message for invalid feed name
        if (!isNameValid.value) {
            Text(
                text = "Name cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Input field for the feed URL
        OutlinedTextField(
            value = feedUrl.value,
            onValueChange = {
                feedUrl.value = it
                isUrlValid.value = isValidUrl(it) // Validate the feed URL
            },
            label = { Text("URL") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth(),
            isError = !isUrlValid.value // Highlight field if invalid
        )

        // Error message for invalid feed URL
        if (!isUrlValid.value) {
            Text(
                text = "Please enter a valid URL",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Instruction text for the user
        Text(
            text = "Enter Full URL of RSS",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Row layout for action buttons (Cancel and Save)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            // Cancel button to navigate back to the Library screen
            Button(onClick = {
                navController.navigate(NavDestination.Library.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true // Save state of the previous screen
                    }
                    launchSingleTop = true // Avoid duplicate instances of the screen
                    restoreState = true // Restore the previous state if available
                }
            }) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Save button to add the RSS feed and navigate back to the Library screen
            Button(
                onClick = {
                    if (isUrlValid.value && isNameValid.value) {
                        viewModel.addFeed(title = feedName.value, url = feedUrl.value) // Add the feed
                        navController.navigate(NavDestination.Library.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                enabled = isNameValid.value && isUrlValid.value // Enable only if inputs are valid
            ) {
                Text("Save")
            }
        }
    }
}
