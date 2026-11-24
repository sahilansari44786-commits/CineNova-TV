package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.UserPreferences
import com.example.data.model.VideoQuality
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "cinenova_preferences")

class UserPreferencesDataStore(private val context: Context) {

    private object Keys {
        val PREFERRED_QUALITY = stringPreferencesKey("preferred_quality")
        val AUTOPLAY_NEXT = booleanPreferencesKey("autoplay_next")
        val SUBTITLES_ENABLED = booleanPreferencesKey("subtitles_enabled")
        val WIFI_ONLY = booleanPreferencesKey("wifi_only_streaming")
    }

    val preferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        val qualityName = prefs[Keys.PREFERRED_QUALITY] ?: VideoQuality.AUTO.name
        val quality = VideoQuality.entries.firstOrNull { it.name == qualityName } ?: VideoQuality.AUTO
        UserPreferences(
            preferredQuality = quality,
            autoplayNext = prefs[Keys.AUTOPLAY_NEXT] ?: true,
            subtitlesEnabled = prefs[Keys.SUBTITLES_ENABLED] ?: false,
            wifiOnlyStreaming = prefs[Keys.WIFI_ONLY] ?: false
        )
    }

    suspend fun setPreferredQuality(quality: VideoQuality) {
        context.dataStore.edit { it[Keys.PREFERRED_QUALITY] = quality.name }
    }

    suspend fun setAutoplayNext(enabled: Boolean) {
        context.dataStore.edit { it[Keys.AUTOPLAY_NEXT] = enabled }
    }

    suspend fun setSubtitlesEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SUBTITLES_ENABLED] = enabled }
    }

    suspend fun setWifiOnlyStreaming(enabled: Boolean) {
        context.dataStore.edit { it[Keys.WIFI_ONLY] = enabled }
    }
}
