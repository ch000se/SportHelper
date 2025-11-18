package com.example.sporthelper.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.sporthelper.domain.model.BodyPart
import com.example.sporthelper.domain.model.User
import com.example.sporthelper.domain.model.predefinedBodyParts
import com.example.sporthelper.presentation.component.ProfileBottomSheet
import com.example.sporthelper.presentation.component.SportHelperDialog
import com.example.sporthelper.presentation.theme.SportHelperTheme
import com.example.sporthelper.presentation.util.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    snackbarHostState: SnackbarHostState,
    uiEvent: Flow<UiEvent>,
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
    onFabClicked: () -> Unit,
    onItemCardClicked: (String) -> Unit,
) {

    var isSignOutDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val isUserAnonymous = state.user?.isAnonymous ?: true
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                UiEvent.HideBottomSheet -> {
                    scope.launch { sheetState.hide() }
                        .invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                isBottomSheetOpen = false
                            }
                        }
                }

                UiEvent.Navigate -> {}
            }
        }
    }

    ProfileBottomSheet(
        isOpen = isBottomSheetOpen,
        onBottomSheetClosed = {
            isBottomSheetOpen = false
        },
        buttonLoadingState = if (isUserAnonymous) state.isSignInButtonLoading else state.isSignOutButtonLoading,
        onGoogleButtonClicked = {
            if (isUserAnonymous) onEvent(DashboardEvent.AnonymousUserSignInWithGoogle(context))
            else isSignOutDialogOpen = true
        },
        buttonPrimaryText = if (isUserAnonymous) "Sign in with Google" else "Sign out with Google",
        user = state.user,
        sheetState = sheetState
    )



    SportHelperDialog(
        onDismissRequest = {
            isSignOutDialogOpen = false
        },
        onConfirmButtonClicked = {
            onEvent(DashboardEvent.SignOut)
            isSignOutDialogOpen = false
        },
        title = "Sign out?",
        body = {
            Text(text = "Are you sure you want to sign out?")
        },
        isOpen = isSignOutDialogOpen
    )

    Scaffold(
        topBar = {
            DashboardTopBar(
                onProfileClicked = { isBottomSheetOpen = true },
                profilePicUrl = state.user?.profilePictureUrl
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    onFabClicked()
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        content = { paddingValues ->
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                columns = GridCells.Adaptive(minSize = 300.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                items(state.bodyPart) { bodyPart ->
                    ItemsCard(
                        bodyPart = bodyPart,
                        onItemCardClicked = onItemCardClicked
                    )
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(
    modifier: Modifier = Modifier,
    profilePicUrl: String? = null,
    onProfileClicked: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "SportHelper"
            )
        },
        actions = {
            IconButton(
                onClick = { onProfileClicked() }
            ) {
                ProfilePicPlaceHolder(
                    placeHolderSize = 30.dp,
                    borderWith = 1.dp,
                    padding = 2.dp,
                    profilePictureURL = profilePicUrl
                )
            }
        }
    )
}

@Composable
private fun ItemsCard(
    modifier: Modifier = Modifier,
    bodyPart: BodyPart,
    onItemCardClicked: (String) -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = { bodyPart.bodyPart?.let { onItemCardClicked(it) } }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(8f),
                text = bodyPart.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${bodyPart.latestValue ?: ""} ${bodyPart.measuringUnit}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to details",
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun DashboardScreenPreview() {
    SportHelperTheme {
        DashboardScreen(
            onFabClicked = {},
            onItemCardClicked = {},
            snackbarHostState = SnackbarHostState(),
            uiEvent = flowOf(),
            state = DashboardState(),
            onEvent = {}
        )
    }

}