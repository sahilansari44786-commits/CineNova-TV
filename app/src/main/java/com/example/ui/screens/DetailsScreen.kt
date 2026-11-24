package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.MediaContent
import com.example.data.model.MediaType
import com.example.ui.components.CineNovaVideoPlayer
import com.example.ui.components.MoviePosterCard
import com.example.ui.components.PosterArtwork
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.CyanSignal
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceHigh
import com.example.ui.theme.SuccessEmerald

@Composable
fun DetailsScreen(
    media: MediaContent?,
    relatedItems: List<MediaContent>,
    inWatchlist: Boolean,
    autoPlay: Boolean,
    onBack: () -> Unit,
    onToggleWatchlist: (String) -> Unit,
    onOpenRelated: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (media == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Media title not found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onBack) {
                Text(stringResource(R.string.action_back))
            }
        }
        return
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 820.dp
        val horizontalPadding = if (isWide) 28.dp else 16.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("details_screen"),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Top bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("details_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = stringResource(R.string.action_back),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Text(
                            text = if (media.type == MediaType.MOVIE) "Movie Details" else "Video Details",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Surface(
                        color = ObsidianSurfaceHigh,
                        border = BorderStroke(1.dp, BorderSubtle),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = media.category,
                            style = MaterialTheme.typography.labelMedium,
                            color = CyanSignal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // Embedded ExoPlayer Video Player
            item {
                Box(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                    CineNovaVideoPlayer(
                        media = media,
                        autoPlay = autoPlay
                    )
                }
            }

            // Poster + Title + Description + Watchlist
            item {
                Column(
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            modifier = Modifier
                                .width(if (isWide) 140.dp else 104.dp)
                                .aspectRatio(3f / 4f),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, BorderSubtle),
                            color = ObsidianSurfaceHigh
                        ) {
                            PosterArtwork(item = media, modifier = Modifier.fillMaxSize())
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = media.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = media.subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MetadataBadge(text = media.qualityBadge, tint = AmberGold)
                                MetadataBadge(text = "${media.releaseYear}", tint = CyanSignal)
                                MetadataBadge(text = media.durationText, tint = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    // Watchlist Action Button
                    if (inWatchlist) {
                        OutlinedButton(
                            onClick = { onToggleWatchlist(media.id) },
                            border = BorderStroke(1.dp, SuccessEmerald.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .testTag("details_watchlist_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.BookmarkAdded,
                                contentDescription = null,
                                tint = SuccessEmerald
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.action_in_watchlist),
                                color = SuccessEmerald
                            )
                        }
                    } else {
                        Button(
                            onClick = { onToggleWatchlist(media.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CrimsonAccent,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .testTag("details_watchlist_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.BookmarkAdd,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.action_add_watchlist))
                        }
                    }

                    // Synopsis Card
                    Surface(
                        color = ObsidianSurface,
                        border = BorderStroke(1.dp, BorderSubtle),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Synopsis",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = media.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Creator: ${media.directorOrCreator}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.VerifiedUser,
                                    contentDescription = null,
                                    tint = SuccessEmerald,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "${stringResource(R.string.licensed_sample_note)} • ${media.licenseSource}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SuccessEmerald
                                )
                            }
                        }
                    }
                }
            }

            // Related Items
            if (relatedItems.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SectionHeader(
                            title = stringResource(R.string.more_like_this),
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = horizontalPadding),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(relatedItems, key = { "rel_${it.id}" }) { rel ->
                                MoviePosterCard(
                                    item = rel,
                                    inWatchlist = false,
                                    onCardClick = { onOpenRelated(rel.id) },
                                    onToggleWatchlist = { onToggleWatchlist(rel.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetadataBadge(
    text: String,
    tint: Color
) {
    Surface(
        color = tint.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.35f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = tint,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
