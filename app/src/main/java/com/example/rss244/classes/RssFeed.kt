package com.example.rss244.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*

    Model

*/

@Entity(tableName = "rss_feeds")
data class RssFeed(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val url: String,
)

/*

    Data Access Object

*/

@Dao
interface RssFeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(feed: RssFeed)

    @Query("SELECT * FROM rss_feeds")
    fun getAllFeeds(): Flow<List<RssFeed>>

    @Delete
    suspend fun deleteFeed(feed: RssFeed)
}

/*

    DataBase

*/

@Database(entities = [RssFeed::class], version = 1)
abstract class RssFeedDatabase : RoomDatabase() {
    abstract fun rssFeedDao(): RssFeedDao
}

/*

    View

*/

class RssFeedViewModel(private val dao: RssFeedDao) : ViewModel() {

    val feeds: StateFlow<List<RssFeed>> = dao.getAllFeeds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addFeed(title: String, url: String) {
        viewModelScope.launch {
            dao.insertFeed(
                RssFeed(
                    title = title, url = url,
                )
            )
        }
    }

    fun deleteFeed(feed: RssFeed) {
        viewModelScope.launch {
            dao.deleteFeed(feed)
        }
    }
}