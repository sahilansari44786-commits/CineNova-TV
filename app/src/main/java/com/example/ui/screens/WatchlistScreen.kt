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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.MediaContent
import com.example.data.model.MediaType
import com.example.ui.components.PosterArtwork
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceHigh
import com.example.ui.viewmodel.CineNovaUiState

@Composable
fun WatchlistScreen(
    state: CineNovaUiState,
    onOpenDetails: (String, Boolean) -> Unit,
    onRemoveWatchlist: (String) -> Unit,
    onExploreHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 720.dp
        val horizontalPadding = if (isWide) 28.dp else 16.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("watchlist_screen")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.watchlist_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Surface(
                    color = ObsidianSurfaceHigh,
                    border = BorderStroke(1.dp, BorderSubtle),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "${state.watchlistItems.size} saved",
                        style = MaterialTheme.typography.labelMedium,
                        color = AmberGold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            if (state.watchlistItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .widthIn(max = 440.dp)
                            .fillMaxWidth(),
                        color = ObsidianSurface,
                        border = BorderStroke(1.dp, BorderSubtle),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                color = CrimsonAccent.copy(alpha = 0.14f),
                                shape = CircleShape,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Rounded.Bookmarks,
                                        contentDescription = null,
                                        tint = CrimsonAccent,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            Text(
                                text = stringResource(R.string.watchlist_empty_title),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.watchlist_empty_subtitle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = onExploreHome,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CrimsonAccent,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("watchlist_explore_button")
                            ) {
                                Text(stringResource(R.string.watchlist_explore_cta))
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 310.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = horizontalPadding,
                        end = horizontalPadding,
                        bottom = 28.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(state.watchlistItems, key = { it.id }) { item ->
                        WatchlistItemCard(
                            item = item,
                            onPlayClick = { onOpenDetails(item.id, true) },
                            onDetailsClick = { onOpenDetails(item.id, false) },
                            onRemoveClick = { onRemoveWatchlist(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WatchlistItemCard(
    item: MediaContent,
    onPlayClick: () -> Unit,
    onDetailsClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onDetailsClick)
            .testTag("watchlist_item_${item.id}"),
        color = ObsidianSurface,
        border = BorderStroke(1.dp, BorderSubtle),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(96.dp)
                    .aspectRatio(16f / 11f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                PosterArtwork(item = item, modifier = Modifier.fillMaxSize())
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (item.type == MediaType.MOVIE) "MOVIE • ${item.category}" else "VIDEO • ${item.category}",
                    style = MaterialTheme.typography.labelSmall,
                    color = CrimsonAccent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("watchlist_play_${item.id}")
                ) {
                    Surface(
                        color = CrimsonAccent,
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Play ${item.title}",
                            tint = Color.White,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("watchlist_remove_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = stringResource(R.string.action_remove),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
