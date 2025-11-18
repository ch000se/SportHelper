package com.example.sporthelper.presentation.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.sporthelper.domain.model.BodyPart
import com.example.sporthelper.domain.model.BodyPartValue
import com.example.sporthelper.domain.model.TimeRange
import com.example.sporthelper.presentation.component.LineGraph
import com.example.sporthelper.presentation.component.NewValueInputBar
import com.example.sporthelper.presentation.component.SportHelperDatePicker
import com.example.sporthelper.presentation.component.SportHelperDialog
import com.example.sporthelper.presentation.component.SportUnitBottomSheet
import com.example.sporthelper.presentation.theme.SportHelperTheme
import com.example.sporthelper.presentation.util.PastOrPresentSelectableDates
import com.example.sporthelper.presentation.util.UiEvent
import com.example.sporthelper.presentation.util.changeLocalDateToDateString
import com.example.sporthelper.presentation.util.changeMillisToLocalDate
import com.example.sporthelper.presentation.util.roundToDecimal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    snackbarHostState: SnackbarHostState,
    windowSize: WindowWidthSizeClass,
    state: DetailsState,
    onEvent: (DetailsEvent) -> Unit,
    uiEvent: Flow<UiEvent>,
    onBackIconClicked: () -> Unit,
    paddingValues: PaddingValues,
) {
    var isInputValueCardVisible by rememberSaveable { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var isDeleteBodyPartDialoge by rememberSaveable { mutableStateOf(false) }
    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(key1 = Unit) {
        uiEvent.collect() { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onEvent(DetailsEvent.RestoreBodyPartValue)
                    }
                }

                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {
                    onBackIconClicked()
                }
            }
        }
    }

    SportUnitBottomSheet(
        isOpen = isBottomSheetOpen,
        onItemClicked = {
            scope.launch { sheetState.hide() }
                .invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        isBottomSheetOpen = false
                    }
                }
            onEvent(DetailsEvent.ChangeMeasuringUnit(it))
        },
        onBottomSheetClosed = { isBottomSheetOpen = false },
        sheetState = sheetState
    )

    SportHelperDialog(
        onDismissRequest = {
            isDeleteBodyPartDialoge = false
        },
        confirmButtonText = "Delete",
        onConfirmButtonClicked = {
            isDeleteBodyPartDialoge = false
            onEvent(DetailsEvent.DeleteBodyPart)
        },
        title = "Delete Body Part?",
        body = {
            Text(text = "Are you sure you want to delete this body part? All the associated data will be lost.")
        },
        isOpen = isDeleteBodyPartDialoge
    )

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = PastOrPresentSelectableDates
    )
    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }

    SportHelperDatePicker(
        state = datePickerState,
        isOpen = isDatePickerDialogOpen,
        onDismissRequest = { isDatePickerDialogOpen = false },
        onConfirmButtonClicked = {
            isDatePickerDialogOpen = false
            onEvent(DetailsEvent.OnDateChanged(datePickerState.selectedDateMillis))
        }
    )


    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DetailsTopBar(
                        onDeleteIconClicked = { isDeleteBodyPartDialoge = true },
                        onBackIconClicked = { onBackIconClicked() },
                        bodyPart = state.bodyPart,
                        onUnitIconClicked = { isBottomSheetOpen = true }
                    )
                    ChartTimeRangeButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        selectedTimeRange = state.timeRange,
                        onClicked = {
                            onEvent(DetailsEvent.OnTimeRangeChanged(it))
                        }
                    )
                    LineGraph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio = 2 / 1f)
                            .padding(16.dp),
                        bodyPartValue = state.graphBodyPartValues
                    )
                    HistorySection(
                        bodyPartValue = state.allBodyPartValues,
                        measuringUnitCode = state.bodyPart?.measuringUnit,
                        onDeleteIconClicked = { onEvent(DetailsEvent.DeleteBodyPartValue(it)) }
                    )
                }
                NewValueInputBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    date = datePickerState.selectedDateMillis.changeMillisToLocalDate()
                        .changeLocalDateToDateString(),
                    isInputValueCardVisible = isInputValueCardVisible,
                    value = state.textFieldValue,
                    onValueChange = {
                        onEvent(DetailsEvent.OnTextFieldValueChanged(it))
                    },
                    onDoneIconClicked = {
                        focusManager.clearFocus()
                        onEvent(DetailsEvent.AddNewValue)
                    },
                    onDoneImeActionClicked = {
                        focusManager.clearFocus()
                    },
                    onCalendarIconClicked = {
                        isDatePickerDialogOpen = true
                    }
                )
                InputCardHideIcon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp),
                    isInputValueCardVisible = isInputValueCardVisible,
                    onClicked = { isInputValueCardVisible = !isInputValueCardVisible }
                )
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                DetailsTopBar(
                    onDeleteIconClicked = { isDeleteBodyPartDialoge = true },
                    onBackIconClicked = { onBackIconClicked() },
                    bodyPart = state.bodyPart,
                    onUnitIconClicked = { isBottomSheetOpen = true }
                )
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ChartTimeRangeButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            selectedTimeRange = state.timeRange,
                            onClicked = {
                                onEvent(DetailsEvent.OnTimeRangeChanged(it))
                            }
                        )
                        LineGraph(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(ratio = 2 / 1f)
                                .padding(16.dp),
                            bodyPartValue = state.graphBodyPartValues
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        HistorySection(
                            bodyPartValue = state.allBodyPartValues,
                            measuringUnitCode = state.bodyPart?.measuringUnit,
                            onDeleteIconClicked = { onEvent(DetailsEvent.DeleteBodyPartValue(it)) }
                        )
                        NewValueInputBar(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            date = datePickerState.selectedDateMillis.changeMillisToLocalDate()
                                .changeLocalDateToDateString(),
                            isInputValueCardVisible = isInputValueCardVisible,
                            value = state.textFieldValue,
                            onValueChange = {
                                onEvent(DetailsEvent.OnTextFieldValueChanged(it))
                            },
                            onDoneIconClicked = {
                                focusManager.clearFocus()
                                onEvent(DetailsEvent.AddNewValue)
                            },
                            onDoneImeActionClicked = {
                                focusManager.clearFocus()
                            },
                            onCalendarIconClicked = {
                                isDatePickerDialogOpen = true
                            }
                        )
                        InputCardHideIcon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 8.dp),
                            isInputValueCardVisible = isInputValueCardVisible,
                            onClicked = { isInputValueCardVisible = !isInputValueCardVisible }
                        )
                    }
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    modifier: Modifier = Modifier,
    bodyPart: BodyPart?,
    onDeleteIconClicked: () -> Unit,
    onBackIconClicked: () -> Unit,
    onUnitIconClicked: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = {
            Text(
                text = bodyPart?.name ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                onClick = { onDeleteIconClicked() }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Add New Icon"
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = bodyPart?.measuringUnit ?: ""
            )
            IconButton(
                onClick = { onUnitIconClicked() }
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "Select Unit Icon"
                )
            }
        }
    )
}

@Composable
private fun ChartTimeRangeButton(
    modifier: Modifier = Modifier,
    selectedTimeRange: TimeRange,
    onClicked: (TimeRange) -> Unit,
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clip(RoundedCornerShape(8.dp))
    ) {
        TimeRange.entries.forEach { timeRange ->
            TimeRangeSelectionButton(
                modifier = Modifier.weight(1f),
                label = timeRange.label,
                labelTextStyle = if (timeRange == selectedTimeRange) {
                    MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    MaterialTheme.typography.labelLarge.copy(
                        color = Color.Gray
                    )
                },
                backgroundColor = if (timeRange == selectedTimeRange) {
                    MaterialTheme.colorScheme.surface
                } else {
                    Color.Transparent
                },
                onClicked = { onClicked(timeRange) }
            )
        }
    }
}

@Composable
private fun TimeRangeSelectionButton(
    modifier: Modifier = Modifier,
    label: String,
    labelTextStyle: TextStyle,
    backgroundColor: Color,
    onClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClicked
            )
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            maxLines = 1,
            style = labelTextStyle
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistorySection(
    modifier: Modifier = Modifier,
    bodyPartValue: List<BodyPartValue>,
    measuringUnitCode: String?,
    onDeleteIconClicked: (BodyPartValue) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        val grouped = bodyPartValue.groupBy { it.date.month }
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "History",
                style = MaterialTheme.typography.headlineSmall,
                textDecoration = TextDecoration.Underline
            )
        }
        grouped.forEach { (month, bodyPartValue) ->
            stickyHeader {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp),
                    text = month.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            items(bodyPartValue, key = { it.date.toString() }) { bodyPartValue ->
                HistoryCard(
                    modifier = Modifier.padding(bottom = 8.dp),
                    bodyPartValue = bodyPartValue,
                    measuringUnitCode = measuringUnitCode,
                    onDeleteIconClicked = { onDeleteIconClicked(bodyPartValue) }
                )
            }
        }

    }
}

@Composable
private fun HistoryCard(
    modifier: Modifier = Modifier,
    bodyPartValue: BodyPartValue,
    measuringUnitCode: String?,
    onDeleteIconClicked: () -> Unit,
) {
    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(horizontal = 5.dp),
                imageVector = Icons.Default.DateRange,
                contentDescription = null
            )
            Text(
                text = bodyPartValue.date.changeLocalDateToDateString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${bodyPartValue.value.roundToDecimal(4)} ${measuringUnitCode.orEmpty()}",
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(
                onClick = { onDeleteIconClicked() }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon"
                )
            }
        }
    }
}

@Composable
private fun InputCardHideIcon(
    modifier: Modifier = Modifier,
    isInputValueCardVisible: Boolean,
    onClicked: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClicked() },
    ) {
        Icon(
            imageVector = if (isInputValueCardVisible) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
            contentDescription = "Hide Keyboard Icon"
        )
    }

}

@PreviewScreenSizes
@Composable
private fun DetailsScreenPreview() {
    SportHelperTheme {
        DetailsScreen(
            windowSize = WindowWidthSizeClass.Expanded,
            onBackIconClicked = {},
            paddingValues = PaddingValues(0.dp),
            snackbarHostState = SnackbarHostState(),
            state = DetailsState(),
            onEvent = {},
            uiEvent = flowOf()
        )
    }

}