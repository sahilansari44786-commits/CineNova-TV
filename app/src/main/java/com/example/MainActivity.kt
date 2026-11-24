package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.OndemandVideo
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ui.components.CineNovaWordmark
import com.example.ui.navigation.AppRoute
import com.example.ui.navigation.TopLevelDestination
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.DetailsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoviesScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SupportScreen
import com.example.ui.screens.VideosScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CineNovaTheme
import com.example.ui.theme.CrimsonAccent
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceHigh
import com.example.ui.theme.ObsidianVoid
import com.example.ui.viewmodel.CineNovaUiState
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(ObsidianVoid.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(ObsidianSurface.toArgb())
        )

        val app = application as CineNovaApplication
        setContent {
            CineNovaTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModel.Factory(app.repository)
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                CineNovaRootApp(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

private data class NavIconPair(
    val selected: ImageVector,
    val unselected: ImageVector
)

private fun TopLevelDestination.icons(): NavIconPair = when (this) {
    TopLevelDestination.HOME -> NavIconPair(Icons.Rounded.Home, Icons.Outlined.Home)
    TopLevelDestination.MOVIES -> NavIconPair(Icons.Rounded.Movie, Icons.Outlined.Movie)
    TopLevelDestination.VIDEOS -> NavIconPair(Icons.Rounded.OndemandVideo, Icons.Outlined.OndemandVideo)
    TopLevelDestination.WATCHLIST -> NavIconPair(Icons.Rounded.Bookmark, Icons.Outlined.BookmarkBorder)
    TopLevelDestination.SETTINGS -> NavIconPair(Icons.Rounded.Settings, Icons.Outlined.Settings)
}

@Composable
fun CineNovaRootApp(
    uiState: CineNovaUiState,
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val isTopLevelRoute = TopLevelDestination.entries.any { dest ->
        currentDestination?.hasRoute(dest.route::class) == true
    } || currentDestination == null

    fun navigateTopLevel(route: AppRoute) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianVoid)
    ) {
        val width = maxWidth

        when {
            // Desktop / Large Tablet: Permanent Navigation Drawer
            width >= 960.dp -> {
                PermanentNavigationDrawer(
                    drawerContent = {
                        PermanentDrawerSheet(
                            modifier = Modifier
                                .width(248.dp)
                                .fillMaxHeight()
                                .windowInsetsPadding(WindowInsets.safeDrawing),
                            drawerContainerColor = ObsidianSurface,
                            drawerContentColor = MaterialTheme.colorScheme.onSurface
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 14.dp, vertical = 18.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CineNovaWordmark(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                TopLevelDestination.entries.forEach { dest ->
                                    val selected = currentDestination?.hasRoute(dest.route::class) == true
                                    val icons = dest.icons()
                                    NavigationDrawerItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(dest.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) icons.selected else icons.unselected,
                                                contentDescription = dest.label
                                            )
                                        },
                                        label = { Text(dest.label) },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = NavigationDrawerItemDefaults.colors(
                                            selectedContainerColor = CrimsonAccent.copy(alpha = 0.18f),
                                            selectedIconColor = CrimsonAccent,
                                            selectedTextColor = Color.White,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.testTag("drawer_${dest.label.lowercase()}")
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = BorderSubtle)
                                Spacer(modifier = Modifier.height(8.dp))

                                val aboutSelected = currentDestination?.hasRoute(AppRoute.AboutRoute::class) == true
                                NavigationDrawerItem(
                                    selected = aboutSelected,
                                    onClick = { navController.navigate(AppRoute.AboutRoute) { launchSingleTop = true } },
                                    icon = { Icon(Icons.Rounded.Info, contentDescription = "About App") },
                                    label = { Text("About App") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = CrimsonAccent.copy(alpha = 0.18f),
                                        selectedIconColor = CrimsonAccent,
                                        selectedTextColor = Color.White,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.testTag("drawer_about")
                                )

                                val supportSelected = currentDestination?.hasRoute(AppRoute.SupportRoute::class) == true
                                NavigationDrawerItem(
                                    selected = supportSelected,
                                    onClick = { navController.navigate(AppRoute.SupportRoute) { launchSingleTop = true } },
                                    icon = { Icon(Icons.Rounded.SupportAgent, contentDescription = "Contact & Support") },
                                    label = { Text("Support") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = CrimsonAccent.copy(alpha = 0.18f),
                                        selectedIconColor = CrimsonAccent,
                                        selectedTextColor = Color.White,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.testTag("drawer_support")
                                )
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                    ) {
                        CineNovaNavHost(
                            navController = navController,
                            uiState = uiState,
                            viewModel = viewModel,
                            onNavigateTopLevel = ::navigateTopLevel
                        )
                    }
                }
            }

            // Medium Tablet / Foldable: Navigation Rail
            width >= 600.dp -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) {
                    NavigationRail(
                        containerColor = ObsidianSurface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        TopLevelDestination.entries.forEach { dest ->
                            val selected = currentDestination?.hasRoute(dest.route::class) == true
                            val icons = dest.icons()
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopLevel(dest.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) icons.selected else icons.unselected,
                                        contentDescription = dest.label
                                    )
                                },
                                label = { Text(dest.label) },
                                colors = NavigationRailItemDefaults.colors(
                                    indicatorColor = CrimsonAccent.copy(alpha = 0.2f),
                                    selectedIconColor = CrimsonAccent,
                                    selectedTextColor = Color.White,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("rail_${dest.label.lowercase()}")
                            )
                        }
                    }

                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        CineNovaNavHost(
                            navController = navController,
                            uiState = uiState,
                            viewModel = viewModel,
                            onNavigateTopLevel = ::navigateTopLevel
                        )
                    }
                }
            }

            // Compact Mobile: Bottom Navigation Bar
            else -> {
                Scaffold(
                    containerColor = ObsidianVoid,
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        if (isTopLevelRoute) {
                            NavigationBar(
                                containerColor = ObsidianSurface,
                                tonalElevation = 0.dp,
                                windowInsets = WindowInsets.navigationBars
                            ) {
                                TopLevelDestination.entries.forEach { dest ->
                                    val selected = currentDestination?.hasRoute(dest.route::class) == true
                                    val icons = dest.icons()
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(dest.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) icons.selected else icons.unselected,
                                                contentDescription = dest.label
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = dest.label,
                                                maxLines = 1
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = CrimsonAccent.copy(alpha = 0.2f),
                                            selectedIconColor = CrimsonAccent,
                                            selectedTextColor = Color.White,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.testTag("bottom_nav_${dest.label.lowercase()}")
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        CineNovaNavHost(
                            navController = navController,
                            uiState = uiState,
                            viewModel = viewModel,
                            onNavigateTopLevel = ::navigateTopLevel
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CineNovaNavHost(
    navController: NavHostController,
    uiState: CineNovaUiState,
    viewModel: MainViewModel,
    onNavigateTopLevel: (AppRoute) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.HomeRoute
    ) {
        composable<AppRoute.HomeRoute> {
            HomeScreen(
                state = uiState,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onOpenDetails = { mediaId, autoPlay ->
                    navController.navigate(AppRoute.DetailsRoute(mediaId, autoPlay))
                },
                onToggleWatchlist = viewModel::toggleWatchlist,
                onNavigateMovies = { onNavigateTopLevel(AppRoute.MoviesRoute) },
                onNavigateVideos = { onNavigateTopLevel(AppRoute.VideosRoute) },
                onNavigateAbout = { navController.navigate(AppRoute.AboutRoute) }
            )
        }

        composable<AppRoute.MoviesRoute> {
            MoviesScreen(
                state = uiState,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onSelectCategory = viewModel::selectMovieCategory,
                onOpenDetails = { mediaId, autoPlay ->
                    navController.navigate(AppRoute.DetailsRoute(mediaId, autoPlay))
                },
                onToggleWatchlist = viewModel::toggleWatchlist
            )
        }

        composable<AppRoute.VideosRoute> {
            VideosScreen(
                state = uiState,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onSelectCategory = viewModel::selectVideoCategory,
                onOpenDetails = { mediaId, autoPlay ->
                    navController.navigate(AppRoute.DetailsRoute(mediaId, autoPlay))
                },
                onToggleWatchlist = viewModel::toggleWatchlist
            )
        }

        composable<AppRoute.WatchlistRoute> {
            WatchlistScreen(
                state = uiState,
                onOpenDetails = { mediaId, autoPlay ->
                    navController.navigate(AppRoute.DetailsRoute(mediaId, autoPlay))
                },
                onRemoveWatchlist = viewModel::removeFromWatchlist,
                onExploreHome = { onNavigateTopLevel(AppRoute.HomeRoute) }
            )
        }

        composable<AppRoute.SettingsRoute> {
            SettingsScreen(
                state = uiState,
                onSelectQuality = viewModel::setPreferredQuality,
                onToggleAutoplay = viewModel::setAutoplayNext,
                onToggleSubtitles = viewModel::setSubtitlesEnabled,
                onToggleWifiOnly = viewModel::setWifiOnlyStreaming,
                onClearWatchlist = viewModel::clearWatchlist,
                onNavigateAbout = { navController.navigate(AppRoute.AboutRoute) },
                onNavigateSupport = { navController.navigate(AppRoute.SupportRoute) }
            )
        }

        composable<AppRoute.DetailsRoute> { entry ->
            val route = entry.toRoute<AppRoute.DetailsRoute>()
            val media = viewModel.getMediaById(route.mediaId)
            val related = media?.let { viewModel.getRelatedMedia(it) }.orEmpty()
            DetailsScreen(
                media = media,
                relatedItems = related,
                inWatchlist = uiState.watchlistIds.contains(route.mediaId),
                autoPlay = route.autoPlay,
                onBack = { navController.popBackStack() },
                onToggleWatchlist = viewModel::toggleWatchlist,
                onOpenRelated = { relatedId ->
                    navController.navigate(AppRoute.DetailsRoute(relatedId, false))
                }
            )
        }

        composable<AppRoute.AboutRoute> {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<AppRoute.SupportRoute> {
            SupportScreen(
                formState = uiState.supportForm,
                onNameChange = viewModel::updateSupportName,
                onEmailChange = viewModel::updateSupportEmail,
                onTopicSelect = viewModel::updateSupportTopic,
                onMessageChange = viewModel::updateSupportMessage,
                onSubmit = viewModel::submitSupportForm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
