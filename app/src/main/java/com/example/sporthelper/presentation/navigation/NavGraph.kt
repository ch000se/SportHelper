package com.example.sporthelper.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sporthelper.presentation.add_item.AddItemScreen
import com.example.sporthelper.presentation.add_item.AddItemViewModel
import com.example.sporthelper.presentation.dashboard.DashBoardViewModel
import com.example.sporthelper.presentation.dashboard.DashboardScreen
import com.example.sporthelper.presentation.details.DetailsScreen
import com.example.sporthelper.presentation.details.DetailsViewModel
import com.example.sporthelper.presentation.signin.SignInScreen
import com.example.sporthelper.presentation.signin.SignInViewModel
import com.example.sporthelper.presentation.util.UiEvent

@Composable
fun NavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowWidthSizeClass,
    paddingValues: PaddingValues,
    signInViewModel: SignInViewModel,
) {


    // CUsing outside the SignInScreen because the sign in func takes time in completing
    // and if we navigate to other screen the LaunchedEffect in SignInScreen will be cancelled
    // and we won't be able to show the snackbar
    LaunchedEffect(key1 = Unit) {
        signInViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {}
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.DashboardScreen,
    ) {

        composable<Routes.SignInScreen> {
            val state by signInViewModel.state.collectAsStateWithLifecycle()
            SignInScreen(
                windowSize = windowSizeClass,
                state = state,
                onEvent = signInViewModel::onEvent,
                paddingValues = paddingValues
            )
        }

        composable<Routes.DashboardScreen> {
            val viewModel: DashBoardViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            DashboardScreen(
                onFabClicked = {
                    navController.navigate(Routes.AddItemScreen)
                },
                onItemCardClicked = { bodyPartId ->
                    navController.navigate(Routes.DetailScreen(bodyPartId))
                },
                snackbarHostState = snackbarHostState,
                uiEvent = viewModel.uiEvent,
                state = state,
                onEvent = viewModel::onEvent
            )
        }

        composable<Routes.AddItemScreen>(
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val viewModel: AddItemViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            AddItemScreen(
                onBackIconClicked = {
                    navController.navigateUp()
                },
                snackbarHostState = snackbarHostState,
                state = state,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent,
                paddingValues = paddingValues
            )
        }

        composable<Routes.DetailScreen>(
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val viewModel: DetailsViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            DetailsScreen(
                windowSize = windowSizeClass,
                onBackIconClicked = {
                    navController.navigateUp()
                },
                snackbarHostState = snackbarHostState,
                state = state,
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent,
                paddingValues = paddingValues
            )
        }
    }
}
