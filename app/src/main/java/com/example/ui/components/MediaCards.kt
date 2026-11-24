package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.MediaContent
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceHigh
import com.example.ui.theme.ObsidianVoid

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(CrimsonAccent, AmberGold)
                        )
                    )
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        if (actionLabel != null && onActionClick != null) {
            Surface(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onActionClick),
                color = Color.Transparent
            ) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = CrimsonAccent,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun PosterArtwork(
    item: MediaContent,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(ObsidianSurfaceHigh)) {
        if (item.posterResId != null) {
            Image(
                painter = painterResource(id = item.posterResId),
                contentDescription = "${item.title} artwork",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val startColor = Color(item.accentStartHex)
            val endColor = Color(item.accentEndHex)
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(startColor, ObsidianSurface, endColor.copy(alpha = 0.72f)),
                        start = Offset.Zero,
                        end = Offset(size.width, size.height)
                    )
                )
                // Atmospheric radial spotlight
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(endColor.copy(alpha = 0.45f), Color.Transparent),
                        center = Offset(size.width * 0.75f, size.height * 0.25f),
                        radius = size.minDimension * 0.75f
                    ),
                    center = Offset(size.width * 0.75f, size.height * 0.25f),
                    radius = size.minDimension * 0.75f
                )
                // Subtle cinematic lens rings
                val ringCenter = Offset(size.width * 0.5f, size.height * 0.44f)
                drawCircle(
                    color = Color.White.copy(alpha = 0.12f),
                    radius = size.minDimension * 0.28f,
                    center = ringCenter,
                    style = Stroke(width = 2.dp.toPx())
                )
                drawCircle(
                    color = endColor.copy(alpha = 0.35f),
                    radius = size.minDimension * 0.17f,
                    center = ringCenter,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
            // Monogram watermark inside decorative lens
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.title.take(2).uppercase(),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
        // Subtle bottom vignette for readable overlays
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            ObsidianVoid.copy(alpha = 0.75f)
                        )
                    )
                )
        )
    }
}

@Composable
fun HeroFeaturedBanner(
    item: MediaContent,
    inWatchlist: Boolean,
    onPlayClick: () -> Unit,
    onDetailsClick: () -> Unit,
    onToggleWatchlist: () -> Unit,
    isWideLayout: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_featured_banner"),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, BorderSubtle),
        color = ObsidianSurface
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isWideLayout) 410.dp else 440.dp)
        ) {
            Image(
                painter = painterResource(
                    id = item.posterResId ?: R.drawable.img_featured_solaris
                ),
                contentDescription = "${item.title} featured banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Multi-stop cinematic scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                ObsidianVoid.copy(alpha = 0.25f),
                                ObsidianVoid.copy(alpha = 0.55f),
                                ObsidianVoid.copy(alpha = 0.96f)
                            )
                        )
                    )
            )
            if (isWideLayout) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    ObsidianVoid.copy(alpha = 0.88f),
                                    ObsidianVoid.copy(alpha = 0.45f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(if (isWideLayout) 28.dp else 20.dp)
                    .fillMaxWidth(if (isWideLayout) 0.65f else 1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = CrimsonAccent,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = stringResource(R.string.featured_badge),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                    Surface(
                        color = ObsidianSurfaceHigh.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = item.qualityBadge,
                            style = MaterialTheme.typography.labelSmall,
                            color = AmberGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = "${item.releaseYear} • ${item.durationText}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = item.title,
                    style = if (isWideLayout) {
                        MaterialTheme.typography.displaySmall
                    } else {
                        MaterialTheme.typography.headlineLarge
                    },
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onPlayClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CrimsonAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("hero_play_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.action_play_now),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    FilledTonalButton(
                        onClick = onToggleWatchlist,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("hero_watchlist_button")
                    ) {
                        Icon(
                            imageVector = if (inWatchlist) {
                                Icons.Rounded.BookmarkAdded
                            } else {
                                Icons.Outlined.BookmarkAdd
                            },
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (inWatchlist) "Saved" else "Watchlist",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    OutlinedButton(
                        onClick = onDetailsClick,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("hero_details_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = stringResource(R.string.action_details),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoviePosterCard(
    item: MediaContent,
    inWatchlist: Boolean,
    onCardClick: () -> Unit,
    onToggleWatchlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(152.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onCardClick)
            .testTag("movie_card_${item.id}")
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BorderSubtle),
            color = ObsidianSurfaceHigh
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                PosterArtwork(item = item, modifier = Modifier.fillMaxSize())

                // Quality tag
                Surface(
                    color = ObsidianVoid.copy(alpha = 0.78f),
                    border = BorderStroke(0.75.dp, AmberGold.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = item.qualityBadge,
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Watchlist bookmark button (48dp touch target)
                IconButton(
                    onClick = onToggleWatchlist,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .minimumInteractiveComponentSize()
                        .testTag("watchlist_toggle_${item.id}")
                ) {
                    Surface(
                        color = ObsidianVoid.copy(alpha = 0.72f),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = if (inWatchlist) {
                                Icons.Rounded.BookmarkAdded
                            } else {
                                Icons.Outlined.BookmarkAdd
                            },
                            contentDescription = if (inWatchlist) {
                                stringResource(R.string.action_in_watchlist)
                            } else {
                                stringResource(R.string.action_add_watchlist)
                            },
                            tint = if (inWatchlist) CrimsonAccent else Color.White,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(18.dp)
                        )
                    }
                }

                // Category chip at bottom-left
                Surface(
                    color = ObsidianVoid.copy(alpha = 0.78f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = item.durationText,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${item.category} • ${item.releaseYear}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun VideoChannelCard(
    item: MediaContent,
    inWatchlist: Boolean,
    onCardClick: () -> Unit,
    onToggleWatchlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onCardClick)
            .testTag("video_card_${item.id}"),
        shape = RoundedCornerShape(18.dp),
        color = ObsidianSurface,
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                PosterArtwork(item = item, modifier = Modifier.fillMaxSize())

                // Center Play Circle
                Surface(
                    color = CrimsonAccent.copy(alpha = 0.92f),
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Play ${item.title}",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Category badge
                Surface(
                    color = ObsidianVoid.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    Text(
                        text = item.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Watchlist button
                IconButton(
                    onClick = onToggleWatchlist,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .minimumInteractiveComponentSize()
                        .testTag("video_watchlist_toggle_${item.id}")
                ) {
                    Surface(
                        color = ObsidianVoid.copy(alpha = 0.72f),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = if (inWatchlist) {
                                Icons.Rounded.BookmarkAdded
                            } else {
                                Icons.Outlined.BookmarkAdd
                            },
                            contentDescription = "Toggle Watchlist",
                            tint = if (inWatchlist) CrimsonAccent else Color.White,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(18.dp)
                        )
                    }
                }

                // Duration badge
                Surface(
                    color = ObsidianVoid.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = item.durationText,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
