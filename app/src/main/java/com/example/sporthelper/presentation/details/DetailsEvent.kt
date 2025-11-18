package com.example.sporthelper.presentation.details

import com.example.sporthelper.domain.model.BodyPartValue
import com.example.sporthelper.domain.model.MeasuringUnit
import com.example.sporthelper.domain.model.TimeRange

sealed class DetailsEvent {
    data object DeleteBodyPart : DetailsEvent()
    data object RestoreBodyPartValue : DetailsEvent()
    data object AddNewValue : DetailsEvent()
    data class DeleteBodyPartValue(val bodyPartValue: BodyPartValue) : DetailsEvent()
    data class ChangeMeasuringUnit(val measuringUnit: MeasuringUnit) : DetailsEvent()
    data class OnDateChanged(val millis: Long?) : DetailsEvent()
    data class OnTextFieldValueChanged(val text: String) : DetailsEvent()
    data class OnTimeRangeChanged(val timeRange: TimeRange) : DetailsEvent()
}