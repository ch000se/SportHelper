package com.example.sporthelper.presentation.add_item

import com.example.sporthelper.domain.model.BodyPart

data class AddItemState(
    val textFieldValue: String = "",
    val selectedBodyPart: BodyPart? = null,
    val bodyParts: List<BodyPart> = emptyList()
)
