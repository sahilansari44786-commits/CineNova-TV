package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.CineNovaSearchBar
import com.example.ui.components.VideoChannelCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.ObsidianSurfaceHigh
import com.example.ui.viewmodel.CineNovaUiState

@Composable
fun VideosScreen(
    state: CineNovaUiState,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String) -> Unit,
    onOpenDetails: (String, Boolean) -> Unit,
    onToggleWatchlist: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredVideos = state.allVideos.filter { video ->
        val matchesCategory = state.selectedVideoCategory == "All" ||
            video.category == state.selectedVideoCategory
        val query = state.searchQuery.trim()
        val matchesQuery = query.isEmpty() ||
            video.title.contains(query, ignoreCase = true) ||
            video.category.contains(query, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 720.dp
        val horizontalPadding = if (isWide) 28.dp else 16.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("videos_screen")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.nav_videos),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                CineNovaSearchBar(
                    query = state.searchQuery,
                    onQueryChange = onSearchQueryChange,
                    placeholder = "Search video channels & shorts…"
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.videoCategories) { category ->
                        val selected = state.selectedVideoCategory == category
                        FilterChip(
                            selected = selected,
                            onClick = { onSelectCategory(category) },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CrimsonAccent,
                                selectedLabelColor = Color.White,
                                containerColor = ObsidianSurfaceHigh,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected,
                                borderColor = BorderSubtle,
                                selectedBorderColor = CrimsonAccent
                            ),
                            modifier = Modifier.testTag("video_filter_$category")
                        )
                    }
                }
            }

            if (filteredVideos.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No videos found in this category.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Try another category chip or clear your search term.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 290.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = horizontalPadding,
                        end = horizontalPadding,
                        top = 4.dp,
                        bottom = 28.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredVideos, key = { it.id }) { video ->
                        VideoChannelCard(
                            item = video,
                            inWatchlist = state.watchlistIds.contains(video.id),
                            onCardClick = { onOpenDetails(video.id, true) },
                            onToggleWatchlist = { onToggleWatchlist(video.id) }
                        )
                    }
                }
            }
        }
    }
}
