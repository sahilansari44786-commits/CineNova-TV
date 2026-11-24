package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.CineNovaWordmark
import com.example.ui.components.RihiTvLogoBadge
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.CyanSignal
import com.example.ui.theme.ObsidianSurface

@Composable
fun AboutScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 720.dp
        val horizontalPadding = if (isWide) 28.dp else 16.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("about_screen"),
            contentPadding = PaddingValues(
                horizontal = horizontalPadding,
                vertical = 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("about_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = stringResource(R.string.about_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Brand Showcase Card
            item {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                        .fillMaxWidth(),
                    color = ObsidianSurface,
                    border = BorderStroke(1.dp, BorderSubtle),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CineNovaWordmark()
                        Text(
                            text = "A modern, mobile-first movie and video streaming platform built with Jetpack Compose, Material 3, and Media3 ExoPlayer.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        RihiTvLogoBadge(
                            showTagline = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Developer & App Metadata Card
            item {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                        .fillMaxWidth(),
                    color = ObsidianSurface,
                    border = BorderStroke(1.dp, BorderSubtle),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Code,
                                contentDescription = null,
                                tint = CrimsonAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Application & Developer Credits",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        HorizontalDivider(color = BorderSubtle)

                        AboutKeyValueRow(
                            label = stringResource(R.string.about_app_name_label),
                            value = stringResource(R.string.about_app_name_value)
                        )
                        AboutKeyValueRow(
                            label = stringResource(R.string.about_developer_label),
                            value = stringResource(R.string.about_developer_value)
                        )
                        AboutKeyValueRow(
                            label = stringResource(R.string.about_version_label),
                            value = stringResource(R.string.about_version_value)
                        )
                        AboutKeyValueRow(
                            label = stringResource(R.string.about_creator_label),
                            value = stringResource(R.string.about_creator_value)
                        )
                    }
                }
            }

            // Platform & Licensing Cards
            item {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                        .fillMaxWidth(),
                    color = ObsidianSurface,
                    border = BorderStroke(1.dp, BorderSubtle),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Shield,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = stringResource(R.string.about_licensing_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = stringResource(R.string.about_licensing_body),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        HorizontalDivider(color = BorderSubtle)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Devices,
                                contentDescription = null,
                                tint = CyanSignal,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Adaptive Architecture & Future Firebase Sync",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Designed for phones, foldables, tablets, and desktop screens with Room local persistence and a clean CloudSyncGateway prepared for future Firebase Firestore & Auth integration.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutKeyValueRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
