package com.angelramirez.agresiembre.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")
    object Recommendations : Screen("recommendations_screen")
    object SavedGuides : Screen("saved_guides_screen")
    object Parcels : Screen("parcels_screen")
    object CropDetail : Screen("crop_detail_screen/{cropId}") {
        fun createRoute(cropId: String) = "crop_detail_screen/$cropId"
    }
}