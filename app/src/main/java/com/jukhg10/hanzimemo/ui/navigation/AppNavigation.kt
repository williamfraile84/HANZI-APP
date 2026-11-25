package com.jukhg10.hanzimemo.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jukhg10.hanzimemo.ui.features.about.AboutScreen
import com.jukhg10.hanzimemo.ui.features.camera.CameraRecognitionScreen
import com.jukhg10.hanzimemo.ui.features.camera.CameraRecognitionViewModel
import com.jukhg10.hanzimemo.ui.features.characters.DictionaryScreen
import com.jukhg10.hanzimemo.ui.features.characters.DictionaryViewModel
import com.jukhg10.hanzimemo.ui.features.flashcard.FlashcardScreen
import com.jukhg10.hanzimemo.ui.features.flashcard.FlashcardViewModel
import com.jukhg10.hanzimemo.ui.features.handwriting.HandwritingPracticeScreen
import com.jukhg10.hanzimemo.ui.features.review.ReviewScreen
import com.jukhg10.hanzimemo.ui.features.review.ReviewViewModel
import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val strings = LocalAppStrings.current

    val items = listOf(
        BottomNavItem(strings.navDictionary, Routes.Dictionary.route, Icons.AutoMirrored.Filled.MenuBook),
        BottomNavItem(strings.navFlashcards, Routes.Flashcards.route, Icons.Default.Layers),
        BottomNavItem(strings.navCalligraphy, Routes.Handwriting.route, Icons.Default.Draw),
        BottomNavItem(strings.navCamera, Routes.Camera.route, Icons.Default.CameraAlt),
        BottomNavItem(strings.navProgress, Routes.Review.route, Icons.Default.Assessment),
        BottomNavItem(strings.navAbout, Routes.About.route, Icons.Default.Info)
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar(
                tonalElevation = 6.dp,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                items.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { 
                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            ) 
                        },
                        alwaysShowLabel = false,
                        selected = selected,
                        onClick = {
                            if (currentDestination?.route != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Dictionary.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(250)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(250)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(250)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(250)) }
        ) {
            composable(Routes.Dictionary.route) {
                val viewModel: DictionaryViewModel = hiltViewModel()
                DictionaryScreen(
                    viewModel = viewModel,
                    onCharacterSelected = { id ->
                        navController.navigate(Routes.FlashcardWithId.createRoute("character", id))
                    },
                    onWordSelected = { id ->
                        navController.navigate(Routes.FlashcardWithId.createRoute("word", id))
                    }
                )
            }

            composable(Routes.Flashcards.route) {
                val viewModel: FlashcardViewModel = hiltViewModel()
                FlashcardScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(Routes.Handwriting.route) {
                val dictVm: DictionaryViewModel = hiltViewModel()
                HandwritingPracticeScreen(
                    recognitionEngine = dictVm.recognitionEngine,
                    navController = navController
                )
            }

            composable(Routes.Camera.route) {
                val viewModel: CameraRecognitionViewModel = hiltViewModel()
                CameraRecognitionScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(
                route = Routes.FlashcardWithId.route,
                arguments = listOf(
                    navArgument("type") { type = NavType.StringType },
                    navArgument("id") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type") ?: ""
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                val viewModel: FlashcardViewModel = hiltViewModel()
                FlashcardScreen(
                    viewModel = viewModel,
                    navController = navController,
                    studyItemId = id,
                    itemType = type
                )
            }

            composable(Routes.Review.route) {
                val viewModel: ReviewViewModel = hiltViewModel()
                ReviewScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(Routes.About.route) {
                AboutScreen()
            }
        }
    }
}
