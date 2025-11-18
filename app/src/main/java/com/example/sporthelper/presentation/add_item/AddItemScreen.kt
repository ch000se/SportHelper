package com.example.sporthelper.presentation.add_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.sporthelper.domain.model.predefinedBodyParts
import com.example.sporthelper.presentation.component.SportHelperDialog
import com.example.sporthelper.presentation.theme.SportHelperTheme
import com.example.sporthelper.presentation.util.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun AddItemScreen(
    snackbarHostState: SnackbarHostState,
    state: AddItemState,
    uiEvent: Flow<UiEvent>,
    onEvent: (AddItemEvent) -> Unit,
    onBackIconClicked: () -> Unit,
    paddingValues: PaddingValues,
) {

    var isAddNewItemDialogOpen by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {}
            }
        }
    }

    SportHelperDialog(
        onDismissRequest = {
            isAddNewItemDialogOpen = false
            onEvent(AddItemEvent.OnAddItemDialog)
        },
        confirmButtonText = "Save",
        onConfirmButtonClicked = {
            isAddNewItemDialogOpen = false
            onEvent(AddItemEvent.UpsertItem)
        },
        title = "Add / Update Item",
        body = {
            OutlinedTextField(
                value = state.textFieldValue,
                onValueChange = { onEvent(AddItemEvent.OnTextFieldValueChange(it)) }
            )
        },
        isOpen = isAddNewItemDialogOpen
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        AddItemTopBar(
            onAddItemClicked = { isAddNewItemDialogOpen = true },
            onBackIconClicked = { onBackIconClicked() }
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            items(state.bodyParts) { bodyPart ->
                ItemsCard(
                    name = bodyPart.name,
                    isChecked = bodyPart.isActive,
                    onCheckedChange = { onEvent(AddItemEvent.OnItemIsActiveChange(bodyPart)) },
                    onClicked = {
                        isAddNewItemDialogOpen = true
                        onEvent(AddItemEvent.OnItemClicked(bodyPart))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddItemTopBar(
    modifier: Modifier = Modifier,
    onAddItemClicked: () -> Unit,
    onBackIconClicked: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = {
            Text(
                text = "Add New Item"
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackIconClicked() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Icon"
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onAddItemClicked() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add New Icon"
                )
            }
        }
    )
}

@Composable
private fun ItemsCard(
    modifier: Modifier = Modifier,
    name: String,
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    onClicked: () -> Unit,
) {
    Box(
        modifier = modifier.clickable { onClicked() },
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(8f),
                text = name,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                modifier = Modifier.weight(1f),
                checked = isChecked,
                onCheckedChange = { onCheckedChange() }
            )

        }
    }
}

@PreviewScreenSizes
@Composable
private fun AddItemScreenPreview() {
    SportHelperTheme {
        AddItemScreen(
            onBackIconClicked = {},
            paddingValues = PaddingValues(0.dp),
            snackbarHostState = SnackbarHostState(),
            uiEvent = flowOf(),
            state = AddItemState(),
            onEvent = {}
        )
    }
}