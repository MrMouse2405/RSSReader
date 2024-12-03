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
import com.example.rss244.ui.theme.RSS244Theme


sealed class NavDestination(val title: String, val route: String, val icon: ImageVector) {
    object Feed : NavDestination(title = "Feed", route = "feed", icon = Icons.Outlined.RssFeed)
    object Saved :
        NavDestination(title = "Saved", route = "saved", icon = Icons.Outlined.BookmarkBorder)

    object Library :
        NavDestination(title = "Library", route = "library", icon = Icons.Filled.LocalLibrary)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RSS244Theme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
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

@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination?.route
        val items = listOf(
            NavDestination.Library, NavDestination.Feed, NavDestination.Saved
        )
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                selected = currentDestination == screen.route,
                label = { Text(screen.title) },
                onClick = {
                    if (currentDestination != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )

        }

    }
}


@Composable
fun NavigatedScreen(modifier: Modifier, navController: NavHostController, context: Context) {
    val database = Database.getRssFeedDB(context)
    NavHost(
        navController = navController,
        startDestination = NavDestination.Feed.route,
        modifier = modifier
    ) {
        composable(NavDestination.Feed.route) { FeedScreen() }
        composable(NavDestination.Library.route) {
            LibraryScreen(
                context = context,
                database = database
            )
        }
        composable(NavDestination.Saved.route) { SavedScreen() }
    }
}