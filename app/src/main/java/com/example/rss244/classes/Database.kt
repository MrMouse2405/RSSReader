package com.example.rss244.classes

import android.content.Context
import androidx.room.Room

object Database {
    private var RssFeedDB: RssFeedDatabase? = null

    fun getRssFeedDB(context: Context): RssFeedDatabase {
        if (RssFeedDB == null) {
            synchronized(RssFeedDatabase::class) {
                RssFeedDB = Room.databaseBuilder(
                    context.applicationContext,
                    RssFeedDatabase::class.java,
                    "rss_database"
                ).build()
            }
        }
        return RssFeedDB!!
    }
}