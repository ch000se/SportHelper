package com.example.sporthelper.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes() {
    @Serializable
    data object SignInScreen : Routes()

    @Serializable
    data object DashboardScreen : Routes()

    @Serializable
    data object AddItemScreen : Routes()

    @Serializable
    data class DetailScreen(val bodyPartId: String) : Routes()
}