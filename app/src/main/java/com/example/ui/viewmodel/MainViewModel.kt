package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.MediaContent
import com.example.data.model.UserPreferences
import com.example.data.model.VideoQuality
import com.example.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SupportFormState(
    val name: String = "",
    val email: String = "",
    val selectedTopic: String = "Playback & Streaming",
    val message: String = "",
    val errorText: String? = null,
    val submittedSuccessfully: Boolean = false
)

data class CineNovaUiState(
    val featuredItem: MediaContent,
    val allMovies: List<MediaContent>,
    val allVideos: List<MediaContent>,
    val movieCategories: List<String>,
    val videoCategories: List<String>,
    val selectedMovieCategory: String = "All",
    val selectedVideoCategory: String = "All",
    val searchQuery: String = "",
    val searchResults: List<MediaContent> = emptyList(),
    val watchlistItems: List<MediaContent> = emptyList(),
    val watchlistIds: Set<String> = emptySet(),
    val preferences: UserPreferences = UserPreferences(),
    val cloudProviderName: String = "",
    val cloudTargetPath: String = "",
    val supportForm: SupportFormState = SupportFormState()
)

private data class FilterInputs(
    val searchQuery: String,
    val selectedMovieCategory: String,
    val selectedVideoCategory: String,
    val supportForm: SupportFormState
)

class MainViewModel(
    private val repository: MediaRepository
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("")
    private val selectedMovieCategoryFlow = MutableStateFlow("All")
    private val selectedVideoCategoryFlow = MutableStateFlow("All")
    private val supportFormFlow = MutableStateFlow(SupportFormState())

    private val filterInputsFlow = combine(
        searchQueryFlow,
        selectedMovieCategoryFlow,
        selectedVideoCategoryFlow,
        supportFormFlow
    ) { query, movieCat, videoCat, support ->
        FilterInputs(
            searchQuery = query,
            selectedMovieCategory = movieCat,
            selectedVideoCategory = videoCat,
            supportForm = support
        )
    }

    val uiState: StateFlow<CineNovaUiState> = combine(
        repository.watchlistItems,
        repository.watchlistIds,
        repository.preferences,
        filterInputsFlow
    ) { watchlistItems, watchlistIds, preferences, filters ->
        val trimmedQuery = filters.searchQuery.trim()
        val searchMatches = if (trimmedQuery.isEmpty()) {
            emptyList()
        } else {
            repository.allCatalog.filter { item ->
                item.title.contains(trimmedQuery, ignoreCase = true) ||
                    item.category.contains(trimmedQuery, ignoreCase = true) ||
                    item.subtitle.contains(trimmedQuery, ignoreCase = true)
            }
        }

        val movieCategories = listOf("All") + repository.movies.map { it.category }.distinct()
        val videoCategories = listOf("All") + repository.videos.map { it.category }.distinct()

        CineNovaUiState(
            featuredItem = repository.featuredItem,
            allMovies = repository.movies,
            allVideos = repository.videos,
            movieCategories = movieCategories,
            videoCategories = videoCategories,
            selectedMovieCategory = filters.selectedMovieCategory,
            selectedVideoCategory = filters.selectedVideoCategory,
            searchQuery = filters.searchQuery,
            searchResults = searchMatches,
            watchlistItems = watchlistItems,
            watchlistIds = watchlistIds,
            preferences = preferences,
            cloudProviderName = repository.cloudSyncGateway.providerName,
            cloudTargetPath = repository.cloudSyncGateway.targetCollectionPath,
            supportForm = filters.supportForm
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CineNovaUiState(
            featuredItem = repository.featuredItem,
            allMovies = repository.movies,
            allVideos = repository.videos,
            movieCategories = listOf("All") + repository.movies.map { it.category }.distinct(),
            videoCategories = listOf("All") + repository.videos.map { it.category }.distinct(),
            cloudProviderName = repository.cloudSyncGateway.providerName,
            cloudTargetPath = repository.cloudSyncGateway.targetCollectionPath
        )
    )

    fun getMediaById(id: String): MediaContent? = repository.getById(id)

    fun getRelatedMedia(item: MediaContent): List<MediaContent> = repository.getRelatedItems(item)

    fun onSearchQueryChange(query: String) {
        searchQueryFlow.value = query
    }

    fun selectMovieCategory(category: String) {
        selectedMovieCategoryFlow.value = category
    }

    fun selectVideoCategory(category: String) {
        selectedVideoCategoryFlow.value = category
    }

    fun toggleWatchlist(mediaId: String) {
        val currentlySaved = uiState.value.watchlistIds.contains(mediaId)
        viewModelScope.launch {
            repository.toggleWatchlist(mediaId, currentlySaved)
        }
    }

    fun removeFromWatchlist(mediaId: String) {
        viewModelScope.launch {
            repository.removeFromWatchlist(mediaId)
        }
    }

    fun clearWatchlist() {
        viewModelScope.launch {
            repository.clearWatchlist()
        }
    }

    fun setPreferredQuality(quality: VideoQuality) {
        viewModelScope.launch { repository.setPreferredQuality(quality) }
    }

    fun setAutoplayNext(enabled: Boolean) {
        viewModelScope.launch { repository.setAutoplayNext(enabled) }
    }

    fun setSubtitlesEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setSubtitlesEnabled(enabled) }
    }

    fun setWifiOnlyStreaming(enabled: Boolean) {
        viewModelScope.launch { repository.setWifiOnlyStreaming(enabled) }
    }

    fun updateSupportName(value: String) {
        supportFormFlow.update { it.copy(name = value, errorText = null, submittedSuccessfully = false) }
    }

    fun updateSupportEmail(value: String) {
        supportFormFlow.update { it.copy(email = value, errorText = null, submittedSuccessfully = false) }
    }

    fun updateSupportTopic(topic: String) {
        supportFormFlow.update { it.copy(selectedTopic = topic, errorText = null, submittedSuccessfully = false) }
    }

    fun updateSupportMessage(value: String) {
        supportFormFlow.update { it.copy(message = value, errorText = null, submittedSuccessfully = false) }
    }

    fun submitSupportForm() {
        val current = supportFormFlow.value
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        when {
            current.name.isBlank() -> {
                supportFormFlow.update { it.copy(errorText = "Please enter your name.") }
            }
            !emailRegex.matches(current.email.trim()) -> {
                supportFormFlow.update { it.copy(errorText = "Please enter a valid email address.") }
            }
            current.message.trim().length < 10 -> {
                supportFormFlow.update { it.copy(errorText = "Please describe your question or feedback (at least 10 characters).") }
            }
            else -> {
                supportFormFlow.update {
                    SupportFormState(
                        selectedTopic = current.selectedTopic,
                        submittedSuccessfully = true
                    )
                }
            }
        }
    }

    fun resetSupportConfirmation() {
        supportFormFlow.update { it.copy(submittedSuccessfully = false) }
    }

    class Factory(private val repository: MediaRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}
