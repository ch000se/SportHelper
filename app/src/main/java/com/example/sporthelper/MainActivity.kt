package com.example.sporthelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.sporthelper.domain.model.AuthStatus
import com.example.sporthelper.presentation.navigation.NavGraph
import com.example.sporthelper.presentation.navigation.Routes
import com.example.sporthelper.presentation.signin.SignInViewModel
import com.example.sporthelper.presentation.theme.SportHelperTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            SportHelperTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                val navController = rememberNavController()
                val signInViewModel: SignInViewModel = hiltViewModel()
                val snackbarHostState = remember { SnackbarHostState() }
                val authStatus by signInViewModel.authStatus.collectAsStateWithLifecycle()
                var previousAuthStatus by rememberSaveable {
                    mutableStateOf<AuthStatus?>(null)
                }
                LaunchedEffect(key1 = authStatus) {
                    if (authStatus != previousAuthStatus) {
                        when (authStatus) {
                            AuthStatus.AUTHENTICATED -> {
                                navController.navigate(Routes.DashboardScreen) {
                                    popUpTo(0)
                                }
                            }

                            AuthStatus.UNAUTHENTICATED -> {
                                navController.navigate(Routes.SignInScreen) {
                                    popUpTo(0)
                                }
                            }

                            AuthStatus.LOADING -> {
                            }

                        }
                        previousAuthStatus = authStatus
                    }
                }
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { paddingValues ->
                    NavGraph(
                        navController = navController,
                        windowSizeClass = windowSizeClass.widthSizeClass,
                        paddingValues = paddingValues,
                        snackbarHostState = snackbarHostState,
                        signInViewModel = signInViewModel
                    )
                }

            }
        }
    }
}

