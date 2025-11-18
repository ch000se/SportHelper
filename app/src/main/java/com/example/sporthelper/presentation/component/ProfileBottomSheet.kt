package com.example.sporthelper.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sporthelper.domain.model.User
import com.example.sporthelper.presentation.dashboard.ProfilePicPlaceHolder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    modifier: Modifier = Modifier,
    user: User?,
    sheetState: SheetState,
    isOpen: Boolean,
    buttonPrimaryText: String = "Continue as Guest",
    buttonLoadingState: Boolean,
    onGoogleButtonClicked: () -> Unit,
    onBottomSheetClosed: () -> Unit,
) {
    if (isOpen) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            onDismissRequest = { onBottomSheetClosed() },
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfilePicPlaceHolder(
                    placeHolderSize = 120.dp,
                    borderWith = 2.dp,
                    profilePictureURL = user?.profilePictureUrl,
                    padding = 5.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if(user == null || user.isAnonymous) "Anonymous User" else user.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if(user == null || user.isAnonymous) "anonymous@sporthelper.io" else user.email,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(20.dp))
                GoogleSignInButton(
                    onClicked = { onGoogleButtonClicked() },
                    loadingState = buttonLoadingState,
                    primaryText = buttonPrimaryText

                )
            }
        }
    }
}