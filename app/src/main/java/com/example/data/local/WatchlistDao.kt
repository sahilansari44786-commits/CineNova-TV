package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist ORDER BY addedAtEpochMs DESC")
    fun observeWatchlist(): Flow<List<WatchlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(item: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE mediaId = :mediaId")
    suspend fun removeById(mediaId: String)

    @Query("DELETE FROM watchlist")
    suspend fun clearAll()
}
