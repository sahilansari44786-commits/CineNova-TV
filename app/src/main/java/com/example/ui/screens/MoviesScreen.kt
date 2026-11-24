package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.MoviePosterCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.ObsidianSurfaceHigh
import com.example.ui.viewmodel.CineNovaUiState

@Composable
fun MoviesScreen(
    state: CineNovaUiState,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (String) -> Unit,
    onOpenDetails: (String, Boolean) -> Unit,
    onToggleWatchlist: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredMovies = state.allMovies.filter { movie ->
        val matchesCategory = state.selectedMovieCategory == "All" ||
            movie.category == state.selectedMovieCategory
        val query = state.searchQuery.trim()
        val matchesQuery = query.isEmpty() ||
            movie.title.contains(query, ignoreCase = true) ||
            movie.category.contains(query, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 720.dp
        val horizontalPadding = if (isWide) 28.dp else 16.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("movies_screen")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.nav_movies),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                CineNovaSearchBar(
                    query = state.searchQuery,
                    onQueryChange = onSearchQueryChange,
                    placeholder = "Search movies by title or genre…"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.movieCategories) { category ->
                        val selected = state.selectedMovieCategory == category
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
                            modifier = Modifier.testTag("movie_filter_$category")
                        )
                    }
                }
            }

            if (state.selectedMovieCategory == "All" && state.searchQuery.isBlank()) {
                val grouped = state.allMovies.groupBy { it.category }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    grouped.forEach { (category, movies) ->
                        item(key = "row_$category") {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                SectionHeader(
                                    title = category,
                                    subtitle = "${movies.size} featured streams",
                                    modifier = Modifier.padding(horizontal = horizontalPadding)
                                )
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    items(movies, key = { it.id }) { movie ->
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
                }
            } else {
                if (filteredMovies.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No movies match your filter.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Clear the search query or switch back to All genres.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 152.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = horizontalPadding,
                            vertical = 8.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(filteredMovies, key = { it.id }) { movie ->
                            MoviePosterCard(
                                item = movie,
                                inWatchlist = state.watchlistIds.contains(movie.id),
                                onCardClick = { onOpenDetails(movie.id, false) },
                                onToggleWatchlist = { onToggleWatchlist(movie.id) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
