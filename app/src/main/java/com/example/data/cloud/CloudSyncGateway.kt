package com.example.data.cloud

/**
 * Abstraction layer prepared for future Firebase Firestore & Firebase Auth integration.
 *
 * When Firebase is provisioned in a future milestone:
 * 1. Implement this interface using FirebaseFirestore (`users/{uid}/watchlist`).
 * 2. Gate cloud sync behind FirebaseAuth / Credential Manager Google Sign-In.
 * 3. Mirror Room watchlist mutations to Firestore for cross-device sync.
 */
interface CloudSyncGateway {
    val providerName: String
    val isConnected: Boolean
    val targetCollectionPath: String

    suspend fun onWatchlistAdded(mediaId: String)
    suspend fun onWatchlistRemoved(mediaId: String)
    suspend fun onWatchlistCleared()
}

class FirebasePendingSyncGateway : CloudSyncGateway {
    override val providerName: String = "Firebase Firestore (Planned)"
    override val isConnected: Boolean = false
    override val targetCollectionPath: String = "users/{uid}/watchlist"

    override suspend fun onWatchlistAdded(mediaId: String) {
        // Future hook: Firebase.firestore.collection("users").document(uid).collection("watchlist")
    }

    override suspend fun onWatchlistRemoved(mediaId: String) {
        // Future hook: delete document from Firestore watchlist subcollection
    }

    override suspend fun onWatchlistCleared() {
        // Future hook: batch delete Firestore watchlist subcollection
    }
}
