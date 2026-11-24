package com.example.data.model

import androidx.annotation.DrawableRes

enum class MediaType {
    MOVIE,
    VIDEO
}

enum class VideoQuality(val label: String, val detail: String) {
    AUTO("Auto", "Adapts to connection speed"),
    FHD("1080p Full HD", "Crisp cinema detail"),
    HD("720p HD", "Balanced quality & data"),
    DATA_SAVER("480p Saver", "Low bandwidth streaming")
}

data class MediaContent(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val type: MediaType,
    val category: String,
    val releaseYear: Int,
    val durationText: String,
    val qualityBadge: String,
    val licenseSource: String,
    val streamUrl: String,
    @DrawableRes val posterResId: Int? = null,
    val accentStartHex: Long,
    val accentEndHex: Long,
    val isFeatured: Boolean = false,
    val directorOrCreator: String
)

data class UserPreferences(
    val preferredQuality: VideoQuality = VideoQuality.AUTO,
    val autoplayNext: Boolean = true,
    val subtitlesEnabled: Boolean = false,
    val wifiOnlyStreaming: Boolean = false
)
