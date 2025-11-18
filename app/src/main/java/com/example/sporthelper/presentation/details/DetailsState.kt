package com.example.sporthelper.presentation.details

import com.example.sporthelper.domain.model.BodyPart
import com.example.sporthelper.domain.model.BodyPartValue
import com.example.sporthelper.domain.model.TimeRange
import java.time.LocalDate

data class DetailsState (
    val bodyPart: BodyPart? = null,
    val textFieldValue: String = "",
    val recentlyDeleteBodyPartValue: BodyPartValue? = null,
    val data: LocalDate = LocalDate.now(),
    val timeRange: TimeRange = TimeRange.LAST_7_DAYS,
    val allBodyPartValues: List<BodyPartValue> = emptyList(),
    val graphBodyPartValues: List<BodyPartValue> = emptyList()
)