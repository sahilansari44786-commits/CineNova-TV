package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.MediaContent
import com.example.data.model.MediaType
import com.example.ui.components.CineNovaSearchBar
import com.example.ui.components.CineNovaWordmark
import com.example.ui.components.HeroFeaturedBanner
import com.example.ui.components.MoviePosterCard
import com.example.ui.components.PosterArtwork
import com.example.ui.components.RihiTvLogoBadge
import com.example.ui.components.SectionHeader
import com.example.ui.components.VideoChannelCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.ObsidianSurface
import com.example.ui.viewmodel.CineNovaUiState

@Composable
fun HomeScreen(
    state: CineNovaUiState,
    onSearchQueryChange: (String) -> Unit,
    onOpenDetails: (String, Boolean) -> Unit,
    onToggleWatchlist: (String) -> Unit,
    onNavigateMovies: () -> Unit,
    onNavigateVideos: () -> Unit,
    onNavigateAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 720.dp
        val horizontalPadding = if (isWide) 28.dp else 16.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_screen_list"),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // Top Branding Row
            item {
                Column(
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (isWide) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CineNovaWordmark()
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                CineNovaSearchBar(
                                    query = state.searchQuery,
                                    onQueryChange = onSearchQueryChange,
                                    modifier = Modifier.width(340.dp)
                                )
                                RihiTvLogoBadge(
                                    onClick = onNavigateAbout,
                                    showTagline = false
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CineNovaWordmark(compact = true)
                            Surface(
                                onClick = onNavigateAbout,
                                color = ObsidianSurface,
                                border = BorderStroke(1.dp, BorderSubtle),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("home_rihi_chip")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Movie,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.rihi_tv_brand),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        CineNovaSearchBar(
                            query = state.searchQuery,
                            onQueryChange = onSearchQueryChange
                        )
                    }
                }
            }

            // Live Search Results Mode
            if (state.searchQuery.isNotBlank()) {
                item {
                    SectionHeader(
                        title = "Search Results (${state.searchResults.size})",
                        subtitle = "Matching \"${state.searchQuery.trim()}\"",
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    )
                }

                if (state.searchResults.isEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontalPadding),
                            color = ObsidianSurface,
                            border = BorderStroke(1.dp, BorderSubtle),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.SearchOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(40.dp)
                                )
                                Text(
                                    text = "No matching movies or videos",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Try searching by title or category such as Sci-Fi, Sintel, or Rihi.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(state.searchResults, key = { it.id }) { match ->
                        SearchResultRow(
                            item = match,
                            onClick = { onOpenDetails(match.id, false) },
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                    }
                }
            } else {
                // Featured Hero Banner
                item {
                    val featured = state.featuredItem
                    HeroFeaturedBanner(
                        item = featured,
                        inWatchlist = state.watchlistIds.contains(featured.id),
                        onPlayClick = { onOpenDetails(featured.id, true) },
                        onDetailsClick = { onOpenDetails(featured.id, false) },
                        onToggleWatchlist = { onToggleWatchlist(featured.id) },
                        isWideLayout = isWide,
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    )
                }

                // Trending Movies Row
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SectionHeader(
                            title = stringResource(R.string.section_trending_movies),
                            subtitle = "Licensed open cinema & sample reels",
                            actionLabel = "See All",
                            onActionClick = onNavigateMovies,
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = horizontalPadding),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(state.allMovies, key = { "trending_${it.id}" }) { movie ->
                                MoviePosterCard(
                                    item = movie,
                                    inWatchlist = state.watchlistIds.contains(movie.id),
                                    onCardClick = { onOpenDetails(movie.id, false) },
                                    onToggleWatchlist = { onToggleWatchlist(movie.id) }
                                )
                            }
                        }
                    }
                }

                // Sci-Fi & Action Row
                val sciFiMovies = state.allMovies.filter { it.category == "Sci-Fi & Action" }
                if (sciFiMovies.isNotEmpty()) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            SectionHeader(
                                title = stringResource(R.string.section_scifi_action),
                                actionLabel = "Browse",
                                onActionClick = onNavigateMovies,
                                modifier = Modifier.padding(horizontal = horizontalPadding)
                            )
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = horizontalPadding),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                items(sciFiMovies, key = { "scifi_${it.id}" }) { movie ->
                                    MoviePosterCard(
                                        item = movie,
                                        inWatchlist = state.watchlistIds.contains(movie.id),
                                        onCardClick = { onOpenDetails(movie.id, false) },
                                        onToggleWatchlist = { onToggleWatchlist(movie.id) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Curated Video Channels Row
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SectionHeader(
                            title = stringResource(R.string.section_featured_videos),
                            subtitle = "Short films, ocean horizons & studio originals",
                            actionLabel = "All Videos",
                            onActionClick = onNavigateVideos,
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = horizontalPadding),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(state.allVideos, key = { "video_${it.id}" }) { video ->
                                VideoChannelCard(
                                    item = video,
                                    inWatchlist = state.watchlistIds.contains(video.id),
                                    onCardClick = { onOpenDetails(video.id, true) },
                                    onToggleWatchlist = { onToggleWatchlist(video.id) },
                                    modifier = Modifier.width(if (isWide) 310.dp else 270.dp)
                                )
                            }
                        }
                    }
                }

                // Rihi TV Partner Studio Showcase
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = horizontalPadding),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SectionHeader(
                            title = "Studio Partner Spotlight",
                            actionLabel = "About",
                            onActionClick = onNavigateAbout
                        )
                        RihiTvLogoBadge(
                            onClick = onNavigateAbout,
                            showTagline = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultRow(
    item: MediaContent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("search_result_${item.id}"),
        color = ObsidianSurface,
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(96.dp)
                    .aspectRatio(16f / 10f)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                PosterArtwork(item = item, modifier = Modifier.fillMaxSize())
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (item.type == MediaType.MOVIE) "MOVIE • ${item.category}" else "VIDEO • ${item.category}",
                    style = MaterialTheme.typography.labelSmall,
                    color = CrimsonAccent
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.durationText} • ${item.qualityBadge}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Open ${item.title}",
                tint = CrimsonAccent,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
