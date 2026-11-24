package com.example.data.repository

import com.example.data.cloud.CloudSyncGateway
import com.example.data.local.UserPreferencesDataStore
import com.example.data.local.WatchlistDao
import com.example.data.local.WatchlistEntity
import com.example.data.model.MediaContent
import com.example.data.model.MediaType
import com.example.data.model.UserPreferences
import com.example.data.model.VideoQuality
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaRepository(
    private val watchlistDao: WatchlistDao,
    private val preferencesDataStore: UserPreferencesDataStore,
    val cloudSyncGateway: CloudSyncGateway
) {
    val allCatalog: List<MediaContent> = CatalogDataSource.items

    val featuredItem: MediaContent
        get() = allCatalog.firstOrNull { it.isFeatured } ?: allCatalog.first()

    val movies: List<MediaContent>
        get() = allCatalog.filter { it.type == MediaType.MOVIE }

    val videos: List<MediaContent>
        get() = allCatalog.filter { it.type == MediaType.VIDEO }

    val preferences: Flow<UserPreferences> = preferencesDataStore.preferencesFlow

    val watchlistIds: Flow<Set<String>> = watchlistDao.observeWatchlist()
        .map { entities -> entities.map { it.mediaId }.toSet() }

    val watchlistItems: Flow<List<MediaContent>> = watchlistDao.observeWatchlist()
        .map { entities ->
            val byId = allCatalog.associateBy { it.id }
            entities.mapNotNull { byId[it.mediaId] }
        }

    fun getById(id: String): MediaContent? = allCatalog.firstOrNull { it.id == id }

    fun getRelatedItems(item: MediaContent): List<MediaContent> {
        val sameCategory = allCatalog.filter { it.id != item.id && it.category == item.category }
        val sameType = allCatalog.filter { it.id != item.id && it.type == item.type && it !in sameCategory }
        return (sameCategory + sameType).take(6)
    }

    suspend fun toggleWatchlist(mediaId: String, currentlySaved: Boolean) {
        if (currentlySaved) {
            watchlistDao.removeById(mediaId)
            cloudSyncGateway.onWatchlistRemoved(mediaId)
        } else {
            watchlistDao.add(WatchlistEntity(mediaId = mediaId))
            cloudSyncGateway.onWatchlistAdded(mediaId)
        }
    }

    suspend fun removeFromWatchlist(mediaId: String) {
        watchlistDao.removeById(mediaId)
        cloudSyncGateway.onWatchlistRemoved(mediaId)
    }

    suspend fun clearWatchlist() {
        watchlistDao.clearAll()
        cloudSyncGateway.onWatchlistCleared()
    }

    suspend fun setPreferredQuality(quality: VideoQuality) =
        preferencesDataStore.setPreferredQuality(quality)

    suspend fun setAutoplayNext(enabled: Boolean) =
        preferencesDataStore.setAutoplayNext(enabled)

    suspend fun setSubtitlesEnabled(enabled: Boolean) =
        preferencesDataStore.setSubtitlesEnabled(enabled)

    suspend fun setWifiOnlyStreaming(enabled: Boolean) =
        preferencesDataStore.setWifiOnlyStreaming(enabled)
}
