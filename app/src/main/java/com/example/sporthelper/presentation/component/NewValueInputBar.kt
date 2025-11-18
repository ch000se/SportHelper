package com.example.sporthelper.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sporthelper.presentation.theme.SportHelperTheme

@Composable
fun NewValueInputBar(
    modifier: Modifier = Modifier,
    date: String,
    value: String,
    isInputValueCardVisible: Boolean,
    onValueChange: (String) -> Unit,
    onDoneIconClicked: () -> Unit,
    onCalendarIconClicked: () -> Unit,
    onDoneImeActionClicked: () -> Unit,
) {

    var inputError by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    inputError = when {
        value.isBlank() -> "Please enter a value"
        value.toFloatOrNull() == null -> "Value must be a number"
        value.toFloatOrNull() == 0f -> "Value can't be zero"
        value.toFloat() < 1f -> "Value must be greater than zero"
        value.toFloat() > 1000f -> "Value must be less than 1000"
        else -> null
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isInputValueCardVisible,
        enter = slideInVertically(
            tween(durationMillis = 600)
        ) { it },
        exit = slideOutVertically(
            tween(durationMillis = 600)
        ) { it }
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(7.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = value,
                onValueChange = { onValueChange(it) },
                singleLine = true,
                isError = inputError != null && value.isNotBlank(),
                supportingText = {
                    Text(
                        text = inputError.orEmpty()
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onDoneImeActionClicked()
                    }
                ),
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = date
                        )
                        IconButton(
                            onClick = { onCalendarIconClicked() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Date Icon"
                            )
                        }
                    }
                }
            )
            FilledIconButton(
                modifier = Modifier.size(50.dp),
                enabled = inputError == null,
                onClick = { onDoneIconClicked() }
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Done Icon"
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewValueInputBarPreview() {
    SportHelperTheme {
        NewValueInputBar(
            value = "",
            date = "10 Aug",
            onValueChange = {},
            onDoneIconClicked = {},
            onDoneImeActionClicked = {},
            onCalendarIconClicked = {},
            isInputValueCardVisible = true
        )
    }

}