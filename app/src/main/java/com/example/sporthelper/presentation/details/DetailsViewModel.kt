package com.example.sporthelper.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.sporthelper.domain.model.BodyPart
import com.example.sporthelper.domain.model.BodyPartValue
import com.example.sporthelper.domain.model.TimeRange
import com.example.sporthelper.domain.repository.DatabaseRepository
import com.example.sporthelper.presentation.add_item.AddItemState
import com.example.sporthelper.presentation.navigation.Routes
import com.example.sporthelper.presentation.util.UiEvent
import com.example.sporthelper.presentation.util.changeMillisToLocalDate
import com.example.sporthelper.presentation.util.roundToDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bodyPartId = savedStateHandle.toRoute<Routes.DetailScreen>().bodyPartId

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(DetailsState())
    val state = combine(
        _state,
        databaseRepository.getBodyPart(bodyPartId),
        databaseRepository.getAllBodyPartValues(bodyPartId)
    ) { state, bodyPart, bodyPartValues ->
        val currentDate = LocalDate.now()
        val last7DaysValues = bodyPartValues.filter { bodyPartValues ->
            bodyPartValues.date.isAfter(currentDate.minusDays(7))
        }
        val last30DaysValues = bodyPartValues.filter { bodyPartValues ->
            bodyPartValues.date.isAfter(currentDate.minusDays(30))
        }
        state.copy(
            bodyPart = bodyPart,
            allBodyPartValues = bodyPartValues,
            graphBodyPartValues = when (state.timeRange) {
                TimeRange.LAST_7_DAYS -> last7DaysValues
                TimeRange.LAST_30_DAYS -> last30DaysValues
                TimeRange.ALL_TIME -> bodyPartValues
            }
        )
    }.catch { e ->
        e.printStackTrace()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailsState()
    )

    fun onEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.AddNewValue -> {
                val state = state.value
                val id = state.allBodyPartValues.find { it.date == state.data }?.bodyPartValueId
                val bodyPartValue = BodyPartValue(
                    value = state.textFieldValue.roundToDecimal(2),
                    date = state.data,
                    bodyPartId = bodyPartId,
                    bodyPartValueId = id
                )
                upsertBodyPartValue(bodyPartValue)
                _state.update {
                    it.copy(textFieldValue = "")
                }
            }

            is DetailsEvent.ChangeMeasuringUnit -> {
                val bodyPart = state.value.bodyPart?.copy(
                    measuringUnit = event.measuringUnit.code
                )
                upsertBodyPart(bodyPart)
            }

            DetailsEvent.DeleteBodyPart -> {
                deleteBodyPart()
            }

            is DetailsEvent.DeleteBodyPartValue -> {
                deleteBodyPartValue(event.bodyPartValue)
                _state.update {
                    it.copy(recentlyDeleteBodyPartValue = event.bodyPartValue)
                }
            }

            is DetailsEvent.OnDateChanged -> {
                val date = event.millis.changeMillisToLocalDate()
                _state.update {
                    it.copy(data = date)
                }
            }

            is DetailsEvent.OnTextFieldValueChanged -> {
                _state.update {
                    it.copy(textFieldValue = event.text)
                }
            }

            is DetailsEvent.OnTimeRangeChanged -> {
                _state.update {
                    it.copy(timeRange = event.timeRange)
                }
            }

            DetailsEvent.RestoreBodyPartValue -> {
                upsertBodyPartValue(state.value.recentlyDeleteBodyPartValue)
                _state.update { it.copy(recentlyDeleteBodyPartValue = null) }
            }
        }
    }

    private fun upsertBodyPart(bodyPart: BodyPart?) {
        viewModelScope.launch {
            bodyPart ?: return@launch
            databaseRepository.upsertBodyPart(bodyPart)
                .onSuccess {
                    _uiEvent.send(UiEvent.ShowSnackbar("Body part saved successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackbar("Something went wrong. ${e.message}"))
                }
        }
    }

    private fun deleteBodyPart() {
        viewModelScope.launch {
            databaseRepository.deleteBodyPart(bodyPartId)
                .onSuccess {
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            message = "Body part deleted successfully",
                            actionLabel = "Undo"
                        )
                    )
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackbar("Something went wrong. ${e.message}"))
                }
        }
    }

    private fun upsertBodyPartValue(bodyPartValue: BodyPartValue?) {
        viewModelScope.launch {
            bodyPartValue ?: return@launch
            databaseRepository.upsertBodyPartValue(bodyPartValue)
                .onSuccess {
                    _uiEvent.send(UiEvent.ShowSnackbar("Body part value saved successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackbar("Something went wrong. ${e.message}"))
                }
        }
    }

    private fun deleteBodyPartValue(bodyPartValue: BodyPartValue) {
        viewModelScope.launch {
            databaseRepository.deleteBodyPartValue(bodyPartValue)
                .onSuccess {
                    _uiEvent.send(UiEvent.ShowSnackbar("Body part value deleted successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackbar("Something went wrong. ${e.message}"))
                }
        }
    }

}