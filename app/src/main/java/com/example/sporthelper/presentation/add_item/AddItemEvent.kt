package com.example.sporthelper.presentation.add_item

import com.example.sporthelper.domain.model.BodyPart

sealed class AddItemEvent {
    data class OnTextFieldValueChange(val value: String) : AddItemEvent()
    data class OnItemClicked(val bodyPart: BodyPart) : AddItemEvent()
    data class OnItemIsActiveChange(val bodyPart: BodyPart) : AddItemEvent()
    data object OnAddItemDialog: AddItemEvent()
    data object UpsertItem: AddItemEvent()
}