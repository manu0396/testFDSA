package com.example.test.ui.nav

sealed class Screens (val route: String) {

    object MainScreen : Screens("main_screen")

    object Auth : Screens("auth")
}