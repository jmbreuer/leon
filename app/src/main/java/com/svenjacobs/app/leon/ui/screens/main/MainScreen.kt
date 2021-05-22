package com.svenjacobs.app.leon.ui.screens.main

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.svenjacobs.app.leon.BuildConfig
import com.svenjacobs.app.leon.R
import com.svenjacobs.app.leon.services.model.CleaningResult
import com.svenjacobs.app.leon.ui.screens.home.HomeScreen
import com.svenjacobs.app.leon.ui.screens.main.model.MainViewModel
import com.svenjacobs.app.leon.ui.screens.main.model.Screen
import com.svenjacobs.app.leon.ui.screens.settings.SettingsParametersScreen
import com.svenjacobs.app.leon.ui.screens.settings.SettingsScreen
import com.svenjacobs.app.leon.ui.theme.AppTheme

@Composable
fun MainScreen(
    context: Context,
    viewModel: MainViewModel,
) {
    val result by viewModel.result.collectAsState()

    fun onShareButtonClick(result: CleaningResult.Success) {
        val intent = viewModel.buildIntent(result.cleanedText)
        context.startActivity(intent)
    }

    fun onVerifyButtonClick(result: CleaningResult.Success) {
        val intent = viewModel.buildCustomTabIntent()
        intent.launchUrl(context, Uri.parse(result.urls.first()))
    }

    val navController = rememberNavController()
    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Settings
    )

    var isBackVisible by remember { mutableStateOf(false) }

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.primary,
                    title = { Text(text = stringResource(R.string.scaffold_title)) },
                    navigationIcon = if (isBackVisible) ({ NavigationIcon(navController) }) else null
                )
            },
            bottomBar = {
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute =
                        navBackStackEntry?.destination?.route ?: Screen.Home.route

                    bottomNavItems.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(stringResource(screen.resourceId)) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                        )
                    }
                }
            },
            content = { padding ->
                Box(
                    modifier = Modifier.padding(
                        start = padding.calculateStartPadding(layoutDirection = LocalLayoutDirection.current),
                        top = padding.calculateTopPadding(),
                        end = padding.calculateEndPadding(layoutDirection = LocalLayoutDirection.current),
                        bottom = padding.calculateBottomPadding(),
                    )
                ) {
                    BackgroundImage()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                    ) {
                        composable(Screen.Home.route) {
                            isBackVisible = false
                            HomeScreen(
                                result = result,
                                onShareButtonClick = ::onShareButtonClick,
                                onVerifyButtonClick = ::onVerifyButtonClick,
                            )
                        }

                        composable(Screen.Settings.route) { navBackStackEntry ->
                            isBackVisible = false
                            SettingsScreen(
                                viewModel = navViewModel(navBackStackEntry),
                                navController = navController,
                            )
                        }

                        composable(Screen.SettingsParameters.route) { navBackStackEntry ->
                            isBackVisible = true
                            SettingsParametersScreen(
                                viewModel = navViewModel(navBackStackEntry),
                            )
                        }
                    }
                }
            },
        )
    }
}

@Composable
private fun NavigationIcon(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(Icons.Filled.ArrowBack, contentDescription = null)
    }
}

@Composable
private inline fun <reified T : ViewModel> navViewModel(navBackStackEntry: NavBackStackEntry): T =
    viewModel(
        factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
    )

@Composable
private fun BackgroundImage() {
    Image(
        painter = painterResource(if (BuildConfig.DEBUG) R.drawable.background_bug else R.drawable.background_broom),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
    )
}