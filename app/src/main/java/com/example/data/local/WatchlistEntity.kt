package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val mediaId: String,
    val addedAtEpochMs: Long = System.currentTimeMillis()
)
