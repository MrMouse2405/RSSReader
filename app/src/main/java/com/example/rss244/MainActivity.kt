package com.example.rss244

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.RssFeed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.rss244.classes.Database
import com.example.rss244.classes.RssFeedViewModel
import com.example.rss244.classes.SavedNewsViewModel
import com.example.rss244.ui.theme.RSS244Theme

// Sealed class representing navigation destinations
sealed class NavDestination(val title: String, val route: String, val icon: ImageVector?) {
    object Feed : NavDestination(title = "Feed", route = "feed", icon = Icons.Outlined.RssFeed)
    object Saved : NavDestination(title = "Saved", route = "saved", icon = Icons.Outlined.BookmarkBorder)
    object Library : NavDestination(title = "Library", route = "library", icon = Icons.Filled.LocalLibrary)
    object AddRss : NavDestination(title = "Add RSS", route = "add_rss", icon = null)
}

// Main activity hosting the app's UI
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge content rendering
        enableEdgeToEdge()

        setContent {
            // Set up the app theme
            RSS244Theme {
                val navController = rememberNavController()
                // Scaffold provides a structure with a bottom bar
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    // Define navigable screens with padding
                    NavigatedScreen(
                        modifier = Modifier.padding(innerPadding),
                        navController,
                        context = this
                    )
                }
            }
        }
    }
}

// Composable for the bottom navigation bar
@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        // Track the current destination
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination?.route

        // Define navigation items
        val items = listOf(
            NavDestination.Library, NavDestination.Feed, NavDestination.Saved
        )

        // Iterate over each item and create navigation bar buttons
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    screen.icon?.let {
                        Icon(
                            screen.icon,
                            contentDescription = screen.title // Describe the icon for accessibility
                        )
                    }
                },
                selected = currentDestination == screen.route, // Highlight selected item
                label = { Text(screen.title) },
                onClick = {
                    // Navigate to the selected route if not already on it
                    if (currentDestination != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true // Save state of the previous screen
                            }
                            launchSingleTop = true // Avoid duplicate instances of the screen
                            restoreState = true // Restore previous state if available
                        }
                    }
                },
            )
        }
    }
}

// Composable for handling navigation and screen transitions
@Composable
fun NavigatedScreen(modifier: Modifier, navController: NavHostController, context: Context) {
    // Obtain a reference to the database
    val database = Database.getRssFeedDB(context)
    val rssFeedViewModel = RssFeedViewModel(database.rssFeedDao())
    val savedNewsViewModel = SavedNewsViewModel(database.savedNewsDao())

    // Set up the navigation host with defined routes
    NavHost(
        navController = navController,
        startDestination = NavDestination.Feed.route,
        modifier = modifier
    ) {
        // Define composable screens for each navigation route
        composable(NavDestination.Feed.route) {
            FeedScreen(viewModel = rssFeedViewModel, savedNewsViewModel = savedNewsViewModel)
        }
        composable(NavDestination.Library.route) {
            LibraryScreen(
                viewModel = rssFeedViewModel,
                navController = navController
            )
        }
        composable(NavDestination.Saved.route) {
            SavedScreen(viewModel = savedNewsViewModel)
        }
        composable(NavDestination.AddRss.route) {
            AddRssScreen(
                viewModel = rssFeedViewModel,
                navController = navController
            )
        }
    }
}
