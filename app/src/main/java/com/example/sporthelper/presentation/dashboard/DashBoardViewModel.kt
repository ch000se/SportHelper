package com.example.sporthelper.presentation.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sporthelper.domain.repository.AuthRepository
import com.example.sporthelper.domain.repository.DatabaseRepository
import com.example.sporthelper.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        databaseRepository.getSignedInUser(),
        databaseRepository.getAllBodyPartsWithLatestValue()
    ) { state, user, bodyParts ->
        val activeBodyParts = bodyParts.filter { it.isActive }
        state.copy(
            user = user,
            bodyPart = activeBodyParts
        )
    }.catch { e ->
        e.printStackTrace()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )


    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.SignOut -> {
                signOut()
            }

            is DashboardEvent.AnonymousUserSignInWithGoogle -> {
                anonymousUserSignInWithGoogle(event.context)
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _state.update { it.copy(isSignOutButtonLoading = true) }
            authRepository.signOut()
                .onSuccess {
                    _uiEvent.send(UiEvent.HideBottomSheet)
                    _uiEvent.send(UiEvent.ShowSnackbar("Signed out successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.HideBottomSheet)
                    _uiEvent.send(UiEvent.ShowSnackbar("Failed to sign out: ${e.message}"))
                }
            _state.update { it.copy(isSignOutButtonLoading = false) }
        }
    }

    private fun anonymousUserSignInWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isSignInButtonLoading = true) }
            authRepository.anonymousUserSignInWithGoogle(context)
                .onSuccess {
                    databaseRepository.addUser()
                        .onSuccess {
                            _uiEvent.send(UiEvent.HideBottomSheet)
                            _uiEvent.send(UiEvent.ShowSnackbar("Signed in with Google"))
                        }
                        .onFailure { e ->
                            _uiEvent.send(UiEvent.HideBottomSheet)
                            _uiEvent.send(UiEvent.ShowSnackbar("Failed to add user to database: ${e.message}"))
                        }
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackbar("Failed to sign in with Google ${e.message}"))
                }
            _state.update { it.copy(isSignInButtonLoading = false) }
        }
    }
}