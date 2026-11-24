package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CloudQueue
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.HighQuality
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material.icons.rounded.Subtitles
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.VideoQuality
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.CyanSignal
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceHigh
import com.example.ui.viewmodel.CineNovaUiState

@Composable
fun SettingsScreen(
    state: CineNovaUiState,
    onSelectQuality: (VideoQuality) -> Unit,
    onToggleAutoplay: (Boolean) -> Unit,
    onToggleSubtitles: (Boolean) -> Unit,
    onToggleWifiOnly: (Boolean) -> Unit,
    onClearWatchlist: () -> Unit,
    onNavigateAbout: () -> Unit,
    onNavigateSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearDialog by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 720.dp
        val horizontalPadding = if (isWide) 28.dp else 16.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("settings_screen"),
            contentPadding = PaddingValues(
                horizontal = horizontalPadding,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Playback & Streaming Section
            item {
                Column(
                    modifier = Modifier.widthIn(max = 760.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings_playback_section),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        color = ObsidianSurface,
                        border = BorderStroke(1.dp, BorderSubtle),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.HighQuality,
                                    contentDescription = null,
                                    tint = CrimsonAccent
                                )
                                Column {
                                    Text(
                                        text = stringResource(R.string.settings_quality_label),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = state.preferences.preferredQuality.detail,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                VideoQuality.entries.forEach { quality ->
                                    val selected = state.preferences.preferredQuality == quality
                                    FilterChip(
                                        selected = selected,
                                        onClick = { onSelectQuality(quality) },
                                        label = {
                                            Text(
                                                text = quality.label.substringBefore(" "),
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        },
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
                                        modifier = Modifier.testTag("quality_chip_${quality.name}")
                                    )
                                }
                            }

                            HorizontalDivider(color = BorderSubtle)

                            SettingsToggleRow(
                                icon = Icons.Rounded.PlayCircleOutline,
                                title = stringResource(R.string.settings_autoplay_label),
                                subtitle = stringResource(R.string.settings_autoplay_desc),
                                checked = state.preferences.autoplayNext,
                                onCheckedChange = onToggleAutoplay,
                                tag = "toggle_autoplay"
                            )

                            HorizontalDivider(color = BorderSubtle)

                            SettingsToggleRow(
                                icon = Icons.Rounded.Subtitles,
                                title = stringResource(R.string.settings_subtitles_label),
                                subtitle = stringResource(R.string.settings_subtitles_desc),
                                checked = state.preferences.subtitlesEnabled,
                                onCheckedChange = onToggleSubtitles,
                                tag = "toggle_subtitles"
                            )

                            HorizontalDivider(color = BorderSubtle)

                            SettingsToggleRow(
                                icon = Icons.Rounded.Wifi,
                                title = stringResource(R.string.settings_wifi_only_label),
                                subtitle = stringResource(R.string.settings_wifi_only_desc),
                                checked = state.preferences.wifiOnlyStreaming,
                                onCheckedChange = onToggleWifiOnly,
                                tag = "toggle_wifi_only"
                            )
                        }
                    }
                }
            }

            // Library & Cloud Readiness
            item {
                Column(
                    modifier = Modifier.widthIn(max = 760.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings_storage_section),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        color = ObsidianSurface,
                        border = BorderStroke(1.dp, BorderSubtle),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CloudQueue,
                                    contentDescription = null,
                                    tint = CyanSignal
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = stringResource(R.string.settings_firebase_title),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = stringResource(R.string.settings_firebase_desc),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Gateway: ${state.cloudProviderName} • ${state.cloudTargetPath}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CyanSignal
                                    )
                                }
                            }

                            HorizontalDivider(color = BorderSubtle)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(enabled = state.watchlistItems.isNotEmpty()) {
                                        showClearDialog = true
                                    }
                                    .padding(vertical = 6.dp)
                                    .testTag("settings_clear_watchlist_row"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DeleteSweep,
                                        contentDescription = null,
                                        tint = if (state.watchlistItems.isNotEmpty()) {
                                            CrimsonAccent
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                    Column {
                                        Text(
                                            text = stringResource(R.string.settings_clear_watchlist),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${state.watchlistItems.size} items saved locally",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // About & Support Links
            item {
                Column(
                    modifier = Modifier.widthIn(max = 760.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings_more_section),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        color = ObsidianSurface,
                        border = BorderStroke(1.dp, BorderSubtle),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            SettingsNavRow(
                                icon = Icons.Rounded.Info,
                                title = stringResource(R.string.settings_about_item),
                                subtitle = "CineNova tv • Developer Ashik Ansari • v1.0.0",
                                onClick = onNavigateAbout,
                                tag = "settings_about_button"
                            )
                            HorizontalDivider(
                                color = BorderSubtle,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            SettingsNavRow(
                                icon = Icons.Rounded.SupportAgent,
                                title = stringResource(R.string.settings_support_item),
                                subtitle = "Send feedback, report playback issues & FAQ",
                                onClick = onNavigateSupport,
                                tag = "settings_support_button"
                            )
                        }
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor = ObsidianSurfaceHigh,
            title = { Text("Clear Watchlist?") },
            text = { Text("This removes all saved movies and videos from your local watchlist.") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearWatchlist()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonAccent)
                ) {
                    Text("Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CrimsonAccent,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = ObsidianSurfaceHigh
            ),
            modifier = Modifier.testTag(tag)
        )
    }
}

@Composable
private fun SettingsNavRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CrimsonAccent,
                modifier = Modifier.size(22.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
