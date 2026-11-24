package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute {
    @Serializable
    data object HomeRoute : AppRoute

    @Serializable
    data object MoviesRoute : AppRoute

    @Serializable
    data object VideosRoute : AppRoute

    @Serializable
    data object WatchlistRoute : AppRoute

    @Serializable
    data object SettingsRoute : AppRoute

    @Serializable
    data class DetailsRoute(
        val mediaId: String,
        val autoPlay: Boolean = false
    ) : AppRoute

    @Serializable
    data object AboutRoute : AppRoute

    @Serializable
    data object SupportRoute : AppRoute
}

enum class TopLevelDestination(
    val route: AppRoute,
    val label: String
) {
    HOME(AppRoute.HomeRoute, "Home"),
    MOVIES(AppRoute.MoviesRoute, "Movies"),
    VIDEOS(AppRoute.VideosRoute, "Videos"),
    WATCHLIST(AppRoute.WatchlistRoute, "Watchlist"),
    SETTINGS(AppRoute.SettingsRoute, "Settings")
}
