package com.example.sporthelper.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sporthelper.presentation.theme.SportHelperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportHelperDialog(
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: () -> Unit,
    confirmButtonText: String = "Yes",
    dismissButtonText: String = "No",
    body: @Composable (() -> Unit)? = null,
    isOpen: Boolean,
    title: String,
    modifier: Modifier = Modifier,
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            title = {
                Text(text = title)
            },
            text = body,
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmButtonClicked()
                    }
                ) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(text = dismissButtonText)
                }
            }
        )
    }
}


@Preview
@Composable
private fun SportHelperDialogPreview() {
    SportHelperTheme {
        SportHelperDialog(
            onDismissRequest = { },
            onConfirmButtonClicked = { },
            title = "Login as anonymous user?",
            body = {
                Text(text = "Are you sure you want to login as an anonymous user? Your data will not be saved and you will not be able to access certain features.")
            },
            isOpen = true
        )
    }

}