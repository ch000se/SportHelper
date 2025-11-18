package com.example.sporthelper.presentation.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.sporthelper.R
import com.example.sporthelper.presentation.component.AnonymousSignInButton
import com.example.sporthelper.presentation.component.GoogleSignInButton
import com.example.sporthelper.presentation.component.SportHelperDialog
import com.example.sporthelper.presentation.theme.SportHelperTheme

@Composable
fun SignInScreen(
    windowSize: WindowWidthSizeClass,
    paddingValues: PaddingValues,
    state: SignInState,
    onEvent: (SignInEvent) -> Unit,
) {
    val context = LocalContext.current
    var isSignInAnonymousDialogOpen by rememberSaveable { mutableStateOf(false) }


    SportHelperDialog(
        onDismissRequest = {
            isSignInAnonymousDialogOpen = false
        },
        onConfirmButtonClicked = {
            onEvent(SignInEvent.SignInAnonymously)
            isSignInAnonymousDialogOpen = false
        },
        title = "Login as anonymous user?",
        body = {
            Text(text = "Are you sure you want to login as an anonymous user? Your data will not be saved and you will not be able to access certain features.")
        },
        isOpen = isSignInAnonymousDialogOpen
    )

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "SportHelper",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Sport is life",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.4f))
                GoogleSignInButton(
                    loadingState = state.isGoogleSignInButtonLoading,
                    enabled = !state.isGoogleSignInButtonLoading && !state.isAnonymousSignInButtonLoading,
                    onClicked = { onEvent(SignInEvent.SignInWithGoogle(context)) }
                )
                Spacer(modifier = Modifier.height(20.dp))
                AnonymousSignInButton(
                    loadingState = state.isAnonymousSignInButtonLoading,
                    enabled = !state.isAnonymousSignInButtonLoading && !state.isGoogleSignInButtonLoading,
                    onClicked = { isSignInAnonymousDialogOpen = true }
                )
            }
        }

        else -> {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "SportHelper",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "Sport is life",
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.4f))
                    GoogleSignInButton(
                        loadingState = state.isGoogleSignInButtonLoading,
                        enabled = !state.isGoogleSignInButtonLoading && !state.isAnonymousSignInButtonLoading,
                        onClicked = { onEvent(SignInEvent.SignInWithGoogle(context)) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    AnonymousSignInButton(
                        loadingState = state.isAnonymousSignInButtonLoading,
                        enabled = !state.isAnonymousSignInButtonLoading && !state.isGoogleSignInButtonLoading,
                        onClicked = { isSignInAnonymousDialogOpen = true }
                    )
                }
            }
        }
    }


}

@PreviewScreenSizes
@Composable
private fun SignInScreenPreview() {
    SportHelperTheme {
        SignInScreen(
            windowSize = WindowWidthSizeClass.Medium,
            paddingValues = PaddingValues(0.dp),
            state = SignInState(),
            onEvent = {}
        )
    }
}