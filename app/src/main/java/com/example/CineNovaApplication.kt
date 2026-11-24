package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.cloud.FirebasePendingSyncGateway
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferencesDataStore
import com.example.data.repository.MediaRepository

class CineNovaApplication : Application() {

    lateinit var repository: MediaRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "cinenova_tv.db"
        ).build()

        val preferencesDataStore = UserPreferencesDataStore(applicationContext)
        val cloudSyncGateway = FirebasePendingSyncGateway()

        repository = MediaRepository(
            watchlistDao = database.watchlistDao(),
            preferencesDataStore = preferencesDataStore,
            cloudSyncGateway = cloudSyncGateway
        )
    }
}
