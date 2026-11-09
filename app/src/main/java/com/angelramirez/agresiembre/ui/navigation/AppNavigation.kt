package com.angelramirez.agresiembre.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.angelramirez.agresiembre.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToRecommendations = {
                    navController.navigate(Screen.Recommendations.route)
                },
                onNavigateToParcels = {
                    navController.navigate(Screen.Parcels.route)
                },
                onNavigateToGuides = {
                    navController.navigate(Screen.SavedGuides.route)
                }
            )
        }

        composable(Screen.Recommendations.route) {
            RecommendationsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToParcels = {
                    navController.navigate(Screen.Parcels.route)
                },
                onNavigateToGuides = {
                    navController.navigate(Screen.SavedGuides.route)
                },
                onNavigateToDetail = { cropId ->
                    navController.navigate(Screen.CropDetail.createRoute(cropId))
                }
            )
        }

        composable(Screen.SavedGuides.route) {
            SavedGuidesScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToRecommendations = {
                    navController.navigate(Screen.Recommendations.route)
                },
                onNavigateToParcels = {
                    navController.navigate(Screen.Parcels.route)
                }
            )
        }

        composable(Screen.Parcels.route) {
            ParcelsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToRecommendations = {
                    navController.navigate(Screen.Recommendations.route)
                },
                onNavigateToGuides = {
                    navController.navigate(Screen.SavedGuides.route)
                }
            )
        }

        composable(
            route = Screen.CropDetail.route,
            arguments = listOf(navArgument("cropId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cropId = backStackEntry.arguments?.getString("cropId") ?: "1"
            CropDetailScreen(
                cropId = cropId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}