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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.RihiTvLogoBadge
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.CyanSignal
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceHigh
import com.example.ui.theme.SuccessEmerald
import com.example.ui.viewmodel.SupportFormState

private data class FaqEntry(val question: String, val answer: String)

private val supportTopics = listOf(
    "Playback & Streaming",
    "Watchlist & Sync",
    "Content Licensing",
    "General Feedback"
)

private val faqList = listOf(
    FaqEntry(
        "Where is my Watchlist stored?",
        "Your Watchlist is saved locally on your device using Room Database so it stays available across app launches, with a repository abstraction ready for future Firebase sync."
    ),
    FaqEntry(
        "What videos are streamed in CineNova TV?",
        "All movies and video channels use licensed Creative Commons open cinema films (Blender Foundation) and official open sample streams."
    ),
    FaqEntry(
        "How do I adjust video streaming preferences?",
        "Open the Settings tab to select your preferred video quality, autoplay behavior, subtitle defaults, and Wi-Fi only streaming."
    )
)

@Composable
fun SupportScreen(
    formState: SupportFormState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTopicSelect: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 720.dp
        val horizontalPadding = if (isWide) 28.dp else 16.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("support_screen"),
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
                            .testTag("support_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = stringResource(R.string.support_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.support_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.widthIn(max = 760.dp)
                )
            }

            // Direct Developer / Studio Support Card
            item {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                        .fillMaxWidth(),
                    color = ObsidianSurface,
                    border = BorderStroke(1.dp, BorderSubtle),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RihiTvLogoBadge(showTagline = true, modifier = Modifier.fillMaxWidth())
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MailOutline,
                                contentDescription = null,
                                tint = CyanSignal,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Developer Desk: Ashik Ansari • support@cinenova.tv",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Interactive Contact Form
            item {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                        .fillMaxWidth(),
                    color = ObsidianSurface,
                    border = BorderStroke(1.dp, BorderSubtle),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (formState.submittedSuccessfully) {
                            Surface(
                                color = SuccessEmerald.copy(alpha = 0.14f),
                                border = BorderStroke(1.dp, SuccessEmerald.copy(alpha = 0.45f)),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("support_success_banner")
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessEmerald
                                    )
                                    Column {
                                        Text(
                                            text = stringResource(R.string.support_sent_title),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = SuccessEmerald
                                        )
                                        Text(
                                            text = stringResource(R.string.support_sent_body),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        val fieldColors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurfaceHigh,
                            unfocusedContainerColor = ObsidianSurfaceHigh,
                            focusedBorderColor = CrimsonAccent,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedTextField(
                            value = formState.name,
                            onValueChange = onNameChange,
                            label = { Text(stringResource(R.string.support_name_label)) },
                            singleLine = true,
                            colors = fieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("support_name_input")
                        )

                        OutlinedTextField(
                            value = formState.email,
                            onValueChange = onEmailChange,
                            label = { Text(stringResource(R.string.support_email_label)) },
                            singleLine = true,
                            colors = fieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("support_email_input")
                        )

                        Text(
                            text = stringResource(R.string.support_topic_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(supportTopics) { topic ->
                                val selected = formState.selectedTopic == topic
                                FilterChip(
                                    selected = selected,
                                    onClick = { onTopicSelect(topic) },
                                    label = { Text(topic) },
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
                                    )
                                )
                            }
                        }

                        OutlinedTextField(
                            value = formState.message,
                            onValueChange = onMessageChange,
                            label = { Text(stringResource(R.string.support_message_label)) },
                            minLines = 4,
                            maxLines = 6,
                            colors = fieldColors,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("support_message_input")
                        )

                        if (formState.errorText != null) {
                            Text(
                                text = formState.errorText,
                                style = MaterialTheme.typography.bodySmall,
                                color = CrimsonAccent
                            )
                        }

                        Button(
                            onClick = onSubmit,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CrimsonAccent,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .testTag("support_submit_button")
                        ) {
                            Text(stringResource(R.string.support_submit))
                        }
                    }
                }
            }

            // FAQs
            item {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 760.dp)
                        .fillMaxWidth(),
                    color = ObsidianSurface,
                    border = BorderStroke(1.dp, BorderSubtle),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = stringResource(R.string.support_faq_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        faqList.forEachIndexed { index, faq ->
                            var expanded by remember { mutableStateOf(index == 0) }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { expanded = !expanded }
                                    .padding(vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = faq.question,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (expanded) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = faq.answer,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            if (index < faqList.lastIndex) {
                                HorizontalDivider(color = BorderSubtle)
                            }
                        }
                    }
                }
            }
        }
    }
}
