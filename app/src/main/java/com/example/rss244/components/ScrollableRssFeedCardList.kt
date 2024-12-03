package com.example.rss244.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rss244.classes.RssFeed
import com.example.rss244.classes.RssFeedViewModel

@Composable
fun ScrollableRssFeedCardList(feeds: List<RssFeed>,viewModel: RssFeedViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(feeds) { feed ->
            RssFeedCard(feed = feed,viewModel=viewModel)
        }
    }
}
