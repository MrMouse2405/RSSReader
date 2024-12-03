package com.example.rss244


import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.rss244.classes.RssFeedDatabase
import com.example.rss244.classes.RssFeedViewModel
import com.example.rss244.components.RssFeedCard
import com.example.rss244.components.ScrollableRssFeedCardList

@Composable
fun LibraryScreen(context: Context, database: RssFeedDatabase) {
    val viewModel = RssFeedViewModel(database.rssFeedDao())
    val feeds = viewModel.feeds.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
           items(feeds.value) {feed ->
               RssFeedCard(feed=feed,viewModel=viewModel)
           }
        }
        FloatingActionButton(
            onClick = { viewModel.addFeed("Example Feed", "https://example.com") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Feed")
        }
    }
}

