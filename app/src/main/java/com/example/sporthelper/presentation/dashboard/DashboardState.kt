package com.example.sporthelper.presentation.dashboard

import com.example.sporthelper.domain.model.BodyPart
import com.example.sporthelper.domain.model.User

data class DashboardState(
    val user: User? = null,
    val bodyPart: List<BodyPart> = emptyList(),
    val isSignInButtonLoading: Boolean = false,
    val isSignOutButtonLoading: Boolean = false,
)