package com.example.rss244.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rss244.classes.News

@Composable
fun ScrollableNewsCardList(items: List<News>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            NewsCard(
                News(
                    title = item.title,
                    source = item.source,
                    description = item.description,
                    category = item.category
                )
            )
        }
    }
}


@Composable
fun ScrollableNewsCardListPreview() {
    val sampleNews = listOf(
        News(
            title = "Security Latest",
            source = "www.wired.com",
            description = "Russian Spies Jumped From One Network to Another Via Wi-Fi in an Unprecedented Hack",
            category = "TECH"
        ),
        News(
            title = "Tech News",
            source = "www.techcrunch.com",
            description = "New AI Model Breaks Records in Data Processing Speed",
            category = "AI"
        ),
        News(
            title = "Health Insights",
            source = "www.medicaldaily.com",
            description = "Latest Research Shows Promising Results for Cancer Treatments",
            category = null
        ),
        News(
            title = "Space Exploration",
            source = "www.nasa.gov",
            description = "NASA Discovers Potentially Habitable Exoplanet",
            category = "SPACE"
        )
    )

    ScrollableNewsCardList(items = sampleNews)
}
